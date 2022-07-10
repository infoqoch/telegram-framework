package infoqoch.dictionarybot.integration.dictionarybot;

import infoqoch.dictionarybot.DictionaryUpdateRunner;
import infoqoch.dictionarybot.mock.data.MockUpdate;
import infoqoch.dictionarybot.mock.update.FakeUpdateDispatcherFactory;
import infoqoch.dictionarybot.run.FakeTelegramBot;
import infoqoch.dictionarybot.run.FakeTelegramUpdate;
import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.log.UpdateLog;
import infoqoch.dictionarybot.update.log.repository.UpdateLogJpaRepository;
import infoqoch.dictionarybot.update.request.UpdateRequestCommand;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static infoqoch.dictionarybot.send.Send.Status.REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/*
 UpdateRunner의 중요한 임무 중 하나는 update의 결과를 send에 잘 전달하는 행위이다.
 unit test인 DictionaryUpdateRunnerTest 의 경우, EventPublisher로 동작하는 Events#raise 에 대한 작동 여부를 판별하기 어렵다.
 결론적으로 아래의 테스트는 SpringBootTest로 진행 가능한 Events.raise(new Send(...))의 동작여부에 대하여 테스트 진행할 예정이다.

 테스트에 필요한 객체가 한정되므로 아래와 같이 객체 사용을 제한한다.
 TelegramBot은 대역을 사용한다.
 repository는 MemoryUpdateLogRepository를 사용하려 하였으나, jpa 간 충돌이 발생한다. 그러므로 autowired한다.
 FakeTelegramUpdate을 통해 가상의 update를 전달한다.
*/

@ActiveProfiles({"test", "fake_send_listener"})
@SpringBootTest
@Transactional
class DictionaryUpdateRunnerIntegrationTest {

    @Autowired
    UpdateLogJpaRepository repository;

    // fake
    TelegramBot bot;
    FakeTelegramUpdate telegramUpdate;
    UpdateDispatcher updateDispatcher;

    // 타겟 객체
    DictionaryUpdateRunner dictionaryUpdateRunner;

    // 타겟 객체, 적절한 send를 보내는지 확인한다.
    @Autowired FakeSendRequestEventListener fakeSendRequestEventListener;

    @BeforeEach
    void setUp(){
        fakeSendRequestEventListener.setCalled(false); // 빈을 재생성하지 않고 기본 값인 false로 롤백한다.

        generateFakeBot();
        updateDispatcher = FakeUpdateDispatcherFactory.defaultInstance();
        dictionaryUpdateRunner = new DictionaryUpdateRunner(bot, updateDispatcher, repository);
    }

    private void generateFakeBot() {
        telegramUpdate = new FakeTelegramUpdate();
        bot = new FakeTelegramBot(telegramUpdate, null);
    }

    @Test
    void lookupByWord(){
        // given
        assert !fakeSendRequestEventListener.isCalled();
        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/w hihi", 123l));

        // when
        dictionaryUpdateRunner.run();

        // then
        final List<UpdateLog> logs = repository.findAll();

        assertThat(logs).size().isEqualTo(1);
        assertThat(logs.get(0).getUpdateCommand()).isEqualTo(UpdateRequestCommand.LOOKUP_WORD);
        assertThat(logs.get(0).getUpdateValue()).isEqualTo("hihi");
        assertThat(logs.get(0).getSendMessage()).isEqualTo("LOOKUP\\_WORD : hihi : 2102");

        assertThat(fakeSendRequestEventListener.isCalled()).isTrue();

        final Send savedSend = fakeSendRequestEventListener.getSavedSend();
        assertThat(savedSend.status()).isEqualTo(REQUEST);
        // TODO 리턴의 데이터 수준 고민
        // assertThat(savedSend.getRequest().message().toString()).isEqualTo("LOOKUP\\_WORD : hihi : 2102");
    }

    // TODO
    // update 자체를 받지 못할 경우 할 수 있는 대응이 없다.
    // 다만, admin에게 해당 내용을 전달할 수 있다. 이 부분은 차후 개발한다.
    @Test
    void exception_get_update_from_telegram(){
        // given
        assert !fakeSendRequestEventListener.isCalled();
        telegramUpdate.setThrowException(true);

        // then
        assertThatThrownBy(()-> dictionaryUpdateRunner.run()).isInstanceOf(RuntimeException.class);
    }

    // update를 가져왔고 이를 분석하는 과정에서 예외가 발생한다.
    // 이때부터는 어떤 update_id에서 문제가 발생하였는지를 명확하게 알 수 있다. 그러므로 이에 합당한 에러 메시지를 텔래그램을 통해 응답할 수 있다.
    // send를 통해 적절한 메시지를 응답하는지 확인한다.
    @Test
    void exception_resolve_update(){
        // given
        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/help exception!!", 123l));

        // when
        dictionaryUpdateRunner.run();

        // then
        final List<UpdateLog> logs = repository.findAll();

        assertThat(logs).size().isEqualTo(1);
        assertThat(logs.get(0).getSendMessage()).isEqualTo(new MarkdownStringBuilder("잘못된 값을 입력하였습니다! 확인 바랍니다.").toString());

        assertThat(fakeSendRequestEventListener.isCalled()).isTrue();
        final Send savedSend = fakeSendRequestEventListener.getSavedSend();
        assertThat(savedSend.status()).isEqualTo(REQUEST);
        // assertThat(savedSend.getRequest().message().toString()).isEqualTo(new MarkdownStringBuilder("잘못된 값을 입력하였습니다! 확인 바랍니다.").toString());
    }
}