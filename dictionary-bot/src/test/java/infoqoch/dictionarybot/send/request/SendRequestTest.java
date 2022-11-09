package infoqoch.dictionarybot.send.request;

import infoqoch.dictionarybot.log.send.SendRequest;
import infoqoch.telegram.framework.update.response.SendType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// SendRequest의 팩터리 메서드의 동작여부를 확인한다.
public class SendRequestTest {
    @DisplayName("SendRequest가 메시지를 전달할 때, 형식에 맞춰 정상적으로 변경되는지 확인")
    @Test
    void body_escape(){
        SendRequest request = SendRequest.sendMessage(112354l, new MarkdownStringBuilder().plain("good job!"));
        assertSendRequest(request, 112354l, SendType.MESSAGE, "good job\\!"); //

        SendRequest request2 = SendRequest.sendMessage(11235344l, new MarkdownStringBuilder().bold("즐거운 일요일!").italic("행복한 코딩시간^^").code("<h3>진짜야!<h3>"));
        assertSendRequest(request2, 11235344l, SendType.MESSAGE, "*즐거운 일요일\\!*_행복한 코딩시간^^_`\\<h3\\>진짜야\\!\\<h3\\>`");
    }

    @DisplayName("SendRequest가 메시지를 전달할 때, 데이터타입에 맞춰 자동으로 보정하는지 확인")
    @Test
    void send_type() {
        SendRequest request = SendRequest.sendMessage(112354l, new MarkdownStringBuilder().plain("good job!"));
        assertSendRequest(request, 112354l, SendType.MESSAGE, "good job\\!"); //

        SendRequest request2 = SendRequest.sendDocument(112354l, "aber2wr", new MarkdownStringBuilder().plain("good job!"));
        assertSendRequest(request2, 112354l, SendType.DOCUMENT, "good job\\!"); //
    }

        private void assertSendRequest(SendRequest request, long chatId, SendType type, String message) {
        assertThat(request.getChatId()).isEqualTo(chatId);
        assertThat(request.getSendType()).isEqualTo(type);
        assertThat(request.getMessage().toString()).isEqualTo(message);
    }

}
