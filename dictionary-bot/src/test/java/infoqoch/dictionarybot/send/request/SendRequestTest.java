package infoqoch.dictionarybot.send.request;

import infoqoch.dictionarybot.send.SendType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SendRequestTest {
    @Test
    void body_escape(){
        SendRequest request = new SendRequest(112354l, SendType.MESSAGE,  new MarkdownStringBuilder().plain("good job!"));
        assertSendRequest(request, 112354l, SendType.MESSAGE, "good job\\!"); //

        SendRequest request2 = new SendRequest(11235344l, SendType.MESSAGE, new MarkdownStringBuilder().bold("즐거운 일요일!").italic("행복한 코딩시간^^").code("<h3>진짜야!<h3>"));
        assertSendRequest(request2, 11235344l, SendType.MESSAGE, "*즐거운 일요일\\!*_행복한 코딩시간^^_`\\<h3\\>진짜야\\!\\<h3\\>`");
    }

    @Test
    void send_type() {
        SendRequest request = new SendRequest(112354l, SendType.MESSAGE, new MarkdownStringBuilder().plain("good job!"));
        assertSendRequest(request, 112354l, SendType.MESSAGE, "good job\\!"); //

        SendRequest request2 = new SendRequest(112354l, SendType.DOCUMENT,  new MarkdownStringBuilder().plain("good job!"));
        assertSendRequest(request2, 112354l, SendType.DOCUMENT, "good job\\!"); //
    }

        private void assertSendRequest(SendRequest request, long chatId, SendType type, String message) {
        assertThat(request.chatId()).isEqualTo(chatId);
        assertThat(request.sendType()).isEqualTo(type);
        assertThat(request.message().toString()).isEqualTo(message);
    }

}
