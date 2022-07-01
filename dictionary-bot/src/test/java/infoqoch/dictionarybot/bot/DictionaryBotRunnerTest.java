package infoqoch.dictionarybot.bot;

import infoqoch.dictionarybot.DictionaryBotRunner;
import infoqoch.dictionarybot.mock.data.MockSendResponse;
import infoqoch.dictionarybot.send.SendDispatcher;
import infoqoch.dictionarybot.mock.update.FakeUpdateDispatcherFactory;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.mock.data.MockUpdate;
import infoqoch.telegrambot.bot.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DictionaryBotRunnerTest {

    // compositions
    TelegramBot bot;
    UpdateDispatcher updateDispatcher;
    SendDispatcher sendDispatcher;
    FakeTelegramSend telegramSend;
    FakeTelegramUpdate telegramUpdate;

    // test target instance
    DictionaryBotRunner runner;

    @BeforeEach
    void setUp(){
        telegramSend = new FakeTelegramSend();
        telegramUpdate = new FakeTelegramUpdate();
        bot = new FakeTelegramBot(telegramUpdate, telegramSend);

        updateDispatcher = FakeUpdateDispatcherFactory.defaultInstance();
        sendDispatcher = new SendDispatcher(bot);
        runner = new DictionaryBotRunner(bot, updateDispatcher, sendDispatcher);
    }
    
    @Test
    void message_send() {
        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/w hi", 123l));
        telegramSend.setMockMessageResponseJson(MockSendResponse.sendMessage("/w hi", 123l));
        runner.run();

        assertThat(telegramSend.isMessageCalled).isTrue(); // spy 로 검사한다.
        assertThat(telegramSend.getMessageResult().isOk()).isTrue();
    }

    @Test
    void message_unknown_command() {
        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/f89j45", 123l));
        telegramSend.setMockMessageResponseJson(MockSendResponse.sendMessage("/w hi", 123l));
        runner.run();

        assertThat(telegramSend.isMessageCalled).isTrue(); // spy 로 검사한다.
        assertThat(telegramSend.getSendMessageRequest().getText()).isEqualTo("unknown??");
    }
}