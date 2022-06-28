package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.bot.FakeTelegramBot;
import infoqoch.dictionarybot.bot.FakeTelegramSend;
import infoqoch.dictionarybot.send.request.SendRequest;
import infoqoch.dictionarybot.send.response.SendResponse;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.response.SendDocumentResponse;
import infoqoch.telegrambot.bot.response.SendMessageResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("가상의 응답값(SendResponse)의 정상처리 여부")
    @Test
    void message(){
        // given. sendRequest를 telegram에 보내고 telegram의 정상 응답값을 받았음. 해당 응답값에 대한 대역
        fakeSend.setMockMessageResponseJson(MockSendResponseGenerate.sendMessage("/help", 12345l));

        // when
        SendResponse sendResponse = sendDispatcher.process(new SendRequest(12345l, SendType.MESSAGE,  new MarkdownStringBuilder().plain("/help")));

        // then
        assertThat(sendResponse.isOk()).isTrue();
        final SendMessageResponse result = (SendMessageResponse) sendResponse.result();
        assertThat(result.getChat().getId()).isEqualTo(12345l);
        assertThat(result.getText()).isEqualTo("/help");
    }

    @DisplayName("가상의 응답값(SendResponse)의 정상처리 여부, sendtype = document")
    @Test
    void document(){
        // given
        fakeSend.setMockDocumentResponseJson(MockSendResponseGenerate.sendDocument(12345l));
        assert fakeSend.isDocumentCalled() == false;

        // when
        final SendResponse response = sendDispatcher.process(new SendRequest(12345l, SendType.DOCUMENT, "fake document",  new MarkdownStringBuilder().plain("fake text")));

        // then
        assertThat(fakeSend.isDocumentCalled()).isTrue();

        final SendDocumentResponse result = (SendDocumentResponse) response.result();
        assertThat(result.getChat().getId()).isEqualTo(12345l);
    }
}
