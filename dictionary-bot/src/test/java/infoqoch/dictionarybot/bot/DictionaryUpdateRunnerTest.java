package infoqoch.dictionarybot.bot;

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
}