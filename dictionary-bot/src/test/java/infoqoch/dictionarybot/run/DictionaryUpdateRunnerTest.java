package infoqoch.dictionarybot.run;

import infoqoch.dictionarybot.main.DictionaryUpdateRunner;
import infoqoch.dictionarybot.mock.data.MockUpdate;
import infoqoch.dictionarybot.mock.repository.MemoryUpdateLogRepository;
import infoqoch.dictionarybot.mock.update.FakeUpdateDispatcherFactory;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.log.UpdateLog;
import infoqoch.dictionarybot.update.request.UpdateRequestCommand;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static infoqoch.dictionarybot.send.SendType.CLIENT_ERROR;
import static infoqoch.dictionarybot.send.SendType.MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class DictionaryUpdateRunnerTest {

    // compositions
    TelegramBot bot;
    UpdateDispatcher updateDispatcher;
    FakeTelegramUpdate telegramUpdate;
    MemoryUpdateLogRepository repository;

    // test target instance
    DictionaryUpdateRunner runner;

    @BeforeEach
    void setUp(){
        telegramUpdate = new FakeTelegramUpdate();
        bot = new FakeTelegramBot(telegramUpdate, null);

        updateDispatcher = FakeUpdateDispatcherFactory.defaultInstance();
        repository = new MemoryUpdateLogRepository();
        runner = new DictionaryUpdateRunner(bot, updateDispatcher, repository);
    }
    
    @Test
    void message_send() {
        // given
        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/w hi", 123l));

        // when
        runner.run();

        // then
        final List<UpdateLog> logs = repository.findAll();

        assertThat(logs).size().isEqualTo(1);
        assertThat(logs.get(0).getUpdateCommand()).isEqualTo(UpdateRequestCommand.LOOKUP_WORD);
        assertThat(logs.get(0).getUpdateValue()).isEqualTo("hi");
        assertThat(logs.get(0).getSendMessage()).isEqualTo("LOOKUP\\_WORD : hi : 2102");
        assertThat(logs.get(0).getSendType()).isEqualTo(MESSAGE);
    }

    @Test
    void message_unknown_command() {
        // given
        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/f89j45", 123l));

        // when
        runner.run();

        // then
        final List<UpdateLog> logs = repository.findAll();

        assertThat(logs).size().isEqualTo(1);
        assertThat(logs.get(0).getUpdateCommand()).isEqualTo(UpdateRequestCommand.UNKNOWN);
        assertThat(logs.get(0).getUpdateValue()).isEqualTo("f89j45");
        assertThat(logs.get(0).getSendMessage()).isEqualTo("unknown??");
    }

    // TODO
    // 현재 조건에서 아래에서 예외가 발생하더라도 처리할 방법이 특별하게 없음.
    // 다만, 차후 모니터링을 위한 기능이 필요. 아마 이 부분은 스프링 컨테이너로 넘어가는 예외 처리하는 것이 나을 수도 있을텐데, 확인 필요.
    @Test
    void throw_update_exception() {
        // given
        telegramUpdate.setThrowException(true);

        // when
        assertThatThrownBy(()->{
            runner.run();
        }).isInstanceOf(RuntimeException.class);
    }

    // update를 분석할 때 예외가 발생한다.
    // FakeController#help의 값이 exception이면 예외를 던지도록 한다.
    @Test
    void throw_resolve_update(){
        // given
        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/help_exception!", 123l));

        // when
        runner.run();

        // then
        final List<UpdateLog> logs = repository.findAll();

        assertThat(logs).size().isEqualTo(1);
        final UpdateLog updateLog = logs.get(0);
        assertThat(updateLog.getSendMessage()).isEqualTo(new MarkdownStringBuilder("잘못된 값을 입력하였습니다! 확인 바랍니다.").toString());
        assertThat(updateLog.getSendType()).isEqualTo(CLIENT_ERROR);
    }


    // update_log를 repository에 등록할 때 실패한다.
    // spy 처리가 까다로워서 단순하게 처리하였다. runner 시 예외가 터지지 않고, repository에 등록된 데이터가 없다.
    @Test
    void throw_update_log_save() {
        // setUp
        // repository를 mocking 한다.
        repository_mocking();

        // given
        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/f89j45", 123l));
        when(repository.save(any())).thenThrow(new RuntimeException("예외!!"));

        // when
        runner.run();

        // then
        final List<UpdateLog> logs = repository.findAll();

        assertThat(logs).size().isEqualTo(0);
    }

    private void repository_mocking() {
        repository = mock(MemoryUpdateLogRepository.class);

        telegramUpdate = new FakeTelegramUpdate();
        bot = new FakeTelegramBot(telegramUpdate, null);

        updateDispatcher = FakeUpdateDispatcherFactory.defaultInstance();
        runner = new DictionaryUpdateRunner(bot, updateDispatcher, repository);
    }
}