package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.send.request.SendRequest;
import infoqoch.dictionarybot.send.response.SendResponse;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.entity.Response;
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
        fakeSend.setMessageResponseJson(generateMockSendMessageResponseBody("/help", 12345l));
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
        fakeSend.setDocumentResponseJson(generateMockSendDocumentResponseBody());
        final SendRequest request = new SendRequest(12345l, SendType.DOCUMENT, "fake document", "fake text");

        // when
        assert fakeSend.isDocumentCalled() == false;
        SendResponse sendResponse = sendDispatcher.process(request);

        // then
        assertThat(fakeSend.isDocumentCalled()).isTrue();
    }

    private String generateMockSendMessageResponseBody(String text, long chatId) {
        return "{\"ok\":true,\"result\":{\"message_id\":2092,\"from\":{\"id\":1959903402,\"is_bot\":true,\"first_name\":\"coffs_test\",\"username\":\"coffs_dic_test_bot\"},\"chat\":{\"id\":" + chatId + ",\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652014357,\"text\":\"" + text + "\"}}";
    }

    private String generateMockSendDocumentResponseBody() {
        return "{\"ok\":true,\"result\":{\"message_id\":2143,\"from\":{\"id\":1959903402,\"is_bot\":true,\"first_name\":\"coffs_test\",\"username\":\"coffs_dic_test_bot\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652609308,\"document\":{\"file_name\":\"sample.xlsx\",\"mime_type\":\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\",\"file_id\":\"BQACAgUAAxkDAAIIX2KA0RyYEZNXxw7qiny1i0Jj7-RqAAL_BAACg56JVdF3guuN7A6tJAQ\",\"file_unique_id\":\"AgAD_wQAAoOeiVU\",\"file_size\":26440},\"caption\":\"\\uc774\\ud0c8\\ub9ad\\uba54\\uc2dc\\uc9c0!\",\"caption_entities\":[{\"offset\":0,\"length\":7,\"type\":\"italic\"}]}}";
    }
}
