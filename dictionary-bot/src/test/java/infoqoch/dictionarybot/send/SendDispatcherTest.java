package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.bot.FakeTelegramBot;
import infoqoch.dictionarybot.bot.FakeTelegramSend;
import infoqoch.dictionarybot.send.request.SendRequest;
import infoqoch.dictionarybot.send.response.SendResponse;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.response.SendMessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SendDispatcherTest {
    SendDispatcher sendDispatcher;
    FakeTelegramSend fakeSend;

    @BeforeEach
    void setUp(){
        fakeSend = new FakeTelegramSend();
        TelegramBot bot = new FakeTelegramBot(fakeSend);
        sendDispatcher = new SendDispatcher(bot);
    }

    @Test
    void message(){
        // given
        fakeSend.setMockMessageResponseJson(MockSendResponseGenerate.sendMessage("/help", 12345l));
        final SendRequest request = new SendRequest(12345l, SendType.MESSAGE, "/help");

        // when
        SendResponse sendResponse = sendDispatcher.process(request);

        // then
        assertThat(sendResponse.isOk()).isTrue();
        final SendMessageResponse result = (SendMessageResponse) sendResponse.result();
        assertThat(result.getChat().getId()).isEqualTo(12345l);
        assertThat(result.getText()).isEqualTo("/help");
    }

    @Test
    void document(){
        // given
        fakeSend.setMockDocumentResponseJson(MockSendResponseGenerate.sendDocument());
        final SendRequest request = new SendRequest(12345l, SendType.DOCUMENT, "fake document", "fake text");

        // when
        assert fakeSend.isDocumentCalled() == false;
        SendResponse sendResponse = sendDispatcher.process(request);

        // then
        assertThat(fakeSend.isDocumentCalled()).isTrue();
    }

}
