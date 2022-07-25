package infoqoch.dictionarybot.main;

import infoqoch.dictionarybot.mock.bot.FakeTelegramBot;
import infoqoch.dictionarybot.mock.bot.FakeTelegramUpdate;
import infoqoch.dictionarybot.mock.data.MockUpdate;
import infoqoch.dictionarybot.mock.repository.MemoryUpdateLogRepository;
import infoqoch.dictionarybot.mock.update.FakeUpdateDispatcherFactory;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.log.UpdateLog;
import infoqoch.dictionarybot.update.request.UpdateRequestCommand;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static infoqoch.dictionarybot.send.SendType.CLIENT_ERROR;
import static infoqoch.dictionarybot.send.SendType.MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class UpdateRunnerTest {

    // compositions
    TelegramBot bot;
    UpdateDispatcher updateDispatcher;
    FakeTelegramUpdate telegramUpdate;
    MemoryUpdateLogRepository repository;

    // test target instance
    UpdateRunner runner;

    @BeforeEach
    void setUp(){
        telegramUpdate = new FakeTelegramUpdate();
        bot = new FakeTelegramBot(telegramUpdate, null);

        updateDispatcher = FakeUpdateDispatcherFactory.defaultInstance();
        repository = new MemoryUpdateLogRepository();
        runner = new UpdateRunner(bot, updateDispatcher, repository);
    }

    @DisplayName("데이터를 정상적으로 읽고 정상적으로 DB에 저장한다.")
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

    @DisplayName("알 수 없는 명령을 보냈으나 정상적으로 처리한다.")
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
    // 현재 조건에서 아래에서 예외가 발생하더라도 처리할 방법이 없음. 러너 밖으로 예외를 던짐.
    // 예외가 발생한다면 텔레그램이 제공하는 json을 해석할 수 없을 때로 예상됨. 하지만 텔래그램 서버에는 동일한 데이터가 계속 존재하므로, 에러가 무한정 발생할 것으로 보임. updateId를 받고 무시할 수 없는 상황이므로.
    // 이에 대한 대응 방안 필요.
    @DisplayName("텔래그램으로부터 데이터를 받았으나 정상 처리하지 못함. 예외가 러너 밖으로 던져짐")
    @Test
    void throw_update_exception() {
        // given
        telegramUpdate.setThrowException(true);

        // when
        assertThatThrownBy(()->{
            runner.run();
        }).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("핸들러 내부의 처리과정에서 예외가 발생한다. ")
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



    @DisplayName("분석한 텔래그램의 데이터를 영속화 하는 과정에서 예외가 발생한다.")
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
        runner = new UpdateRunner(bot, updateDispatcher, repository);
    }
}