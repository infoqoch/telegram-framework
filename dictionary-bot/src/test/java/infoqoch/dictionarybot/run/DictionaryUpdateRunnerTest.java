package infoqoch.dictionarybot.run;

import infoqoch.dictionarybot.DictionaryUpdateRunner;
import infoqoch.dictionarybot.mock.data.MockUpdate;
import infoqoch.dictionarybot.mock.update.FakeUpdateDispatcherFactory;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.log.UpdateLog;
import infoqoch.dictionarybot.update.log.repository.MemoryUpdateLogRepository;
import infoqoch.dictionarybot.update.request.UpdateRequestCommand;
import infoqoch.telegrambot.bot.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        repository = new MemoryUpdateLogRepository();
        telegramUpdate = new FakeTelegramUpdate();
        bot = new FakeTelegramBot(telegramUpdate, null);

        updateDispatcher = FakeUpdateDispatcherFactory.defaultInstance();
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
    
    @Test
    void throw_update_exception() {
        // given
        telegramUpdate.setThrowException(true);

        // when
        // 현재 조건에서 아래에서 예외가 발생하더라도 처리할 방법이 특별하게 없음.
        // TODO
        // 다만, 차후 모니터링을 위한 기능이 필요. 아마 이 부분은 스프링 컨테이너로 넘어가는 예외 처리하는 것이 나을 수도 있을텐데, 확인 필요.
        assertThatThrownBy(()->{
            runner.run();
        }).isInstanceOf(RuntimeException.class);

    }

    // TODO
    // 예외에 대한 테스트 코드를 작성해야 하나, 대역을 만들기 까다롭다.
    // 고민이 든다.
    @Test
    void throw_updateDispatcher_exception() {
        // setUp
        // repository를 mocking 한다.
        setUp_updateDispatcher_exception();

        // given
        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/f89j45", 123l));
        when(repository.save(any())).thenThrow(new RuntimeException("예외!!"));

        // when
        runner.run();
    }

    private void setUp_updateDispatcher_exception() {
        repository = mock(MemoryUpdateLogRepository.class);

        telegramUpdate = new FakeTelegramUpdate();
        bot = new FakeTelegramBot(telegramUpdate, null);

        updateDispatcher = FakeUpdateDispatcherFactory.defaultInstance();
        runner = new DictionaryUpdateRunner(bot, updateDispatcher, repository);
    }
}