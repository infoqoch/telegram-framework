package infoqoch.dictionarybot.integration.dictionarybot;

import infoqoch.dictionarybot.main.DictionaryUpdateRunner;
import infoqoch.dictionarybot.mock.FakeSendRequestEventListener;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static infoqoch.dictionarybot.send.Send.Status.REQUEST;
import static infoqoch.dictionarybot.send.SendType.CLIENT_ERROR;
import static infoqoch.dictionarybot.send.SendType.SERVER_ERROR;
import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.SHARE_MINE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/*
* DictionaryUpdateRunner의 동작을 테스트한다.
* telegram으로부터 실제로 update를 받고 send를 할 수 없으므로 대역을 사용한다.
* Event#raise의 경우 static method이므로 프로파일을 통해 대역을 빈에 등록한다.
* update json의 해석 -> dispatch -> param과 return resolve -> send REQUEST 요청 등 전체적인 흐름을 테스트한다.
*/

@ActiveProfiles({"test", "fake_send_listener"})
@SpringBootTest
@Transactional
class DictionaryUpdateRunnerIntegrationTest {

    @Autowired
    UpdateLogJpaRepository repository;

    // fake
    FakeTelegramUpdate telegramUpdate;
    UpdateDispatcher updateDispatcher;
    @Autowired
    FakeSendRequestEventListener fakeSendRequestEventListener;

    // 타겟 객체
    DictionaryUpdateRunner dictionaryUpdateRunner;

    @BeforeEach
    void setUp(){
        fakeSendRequestEventListener.setCalled(false); // 빈을 재생성하지 않고 기본 값인 false로 롤백한다.
        updateDispatcher = FakeUpdateDispatcherFactory.defaultInstance();
        dictionaryUpdateRunner = new DictionaryUpdateRunner(generateFakeBot(), updateDispatcher, repository);
    }

    private TelegramBot generateFakeBot() {
        telegramUpdate = new FakeTelegramUpdate();
        return new FakeTelegramBot(telegramUpdate, null);
    }

    @Test
    @DisplayName("정상 흐름")
    void lookupByWord(){
        // given
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

        final Send savedSend = fakeSendRequestEventListener.getLatestSent();
        assertThat(savedSend.getStatus()).isEqualTo(REQUEST);
        assertThat(savedSend.result().getRequest().getMessage().toString()).isEqualTo("LOOKUP\\_WORD : hihi : 2102");
    }

    @Test
    @DisplayName("텔래그램에서 update를 받지 못하여 예외가 발생함")
    void exception_get_update_from_telegram(){
        // given
        telegramUpdate.setThrowException(true);

        // then
        assertThatThrownBy(()-> dictionaryUpdateRunner.run()).isInstanceOf(RuntimeException.class);
    }

    // SHARE_MINE 컨트롤러를 사용할 경우 param을 받는 과정에서 예외가 발생한다. 이로 인하여 UpdateDispatcher에 예외가 발생한다.
    @DisplayName("UpdateDispatcher에서 RuntimeException이 발생")
    @Test
    void exception_update_dispatcher(){
        // given
        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/"+SHARE_MINE.value()+" hihi", 123l));

        // when
        dictionaryUpdateRunner.run();

        // then
        final List<UpdateLog> logs = repository.findAll();

        assertThat(logs).size().isEqualTo(1);
        assertThat(logs.get(0).getSendMessage()).isEqualTo(new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (2)").toString());
        assertThat(logs.get(0).getSendType()).isEqualTo(SERVER_ERROR);

        assertThat(fakeSendRequestEventListener.isCalled()).isTrue();

        final Send savedSend = fakeSendRequestEventListener.getLatestSent();
        assertThat(savedSend.getStatus()).isEqualTo(REQUEST);
        assertThat(savedSend.result().getRequest().getMessage().toString()).isEqualTo(new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (2)").toString());
    }

    // @UpdateRequestMethodMapper의 Command.HELP 에서 value에 exception을 넣으면 예외가 발생하도록 하였음.
    @Test
    @DisplayName("@UpdateRequestMethodMapper 에서 데이터 처리할 때 예외가 발생하였음")
    void exception_resolve_update(){
        // given
        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/help exception!!", 123l));

        // when
        dictionaryUpdateRunner.run();

        // then
        final List<UpdateLog> logs = repository.findAll();

        assertThat(logs).size().isEqualTo(1);
        assertThat(logs.get(0).getSendMessage()).isEqualTo(new MarkdownStringBuilder("잘못된 값을 입력하였습니다! 확인 바랍니다.").toString());
        assertThat(logs.get(0).getSendType()).isEqualTo(CLIENT_ERROR);

        assertThat(fakeSendRequestEventListener.isCalled()).isTrue();

        final Send savedSend = fakeSendRequestEventListener.getLatestSent();
        assertThat(savedSend.getStatus()).isEqualTo(REQUEST);
        assertThat(savedSend.result().getRequest().getMessage().toString()).isEqualTo(new MarkdownStringBuilder("잘못된 값을 입력하였습니다! 확인 바랍니다.").toString());
    }
}