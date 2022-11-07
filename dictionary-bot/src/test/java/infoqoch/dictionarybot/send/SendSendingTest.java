package infoqoch.dictionarybot.send;

import infoqoch.mock.data.MockSendResponse;
import infoqoch.mock.bot.FakeTelegramSend;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static infoqoch.dictionarybot.send.Send.Status.REQUEST;
import static infoqoch.dictionarybot.send.Send.Status.SUCCESS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// 텔레그램에 메시지를 전달할 때, 정상적으로 데이터를 전달하고, 응답받은 데이터를 정상적으로 처리하는지 확인한다.
// 특히 TelegramSend를 touch 하는지 여부를 검토한다.
public class SendSendingTest {
    FakeTelegramSend fakeSend;

    @BeforeEach
    void setUp(){
        fakeSend = new FakeTelegramSend();
    }

    @DisplayName("응답값(SendResponse)의 정상처리 여부")
    @Test
    void message(){
        // given
        fakeSend.setMockMessageResponseJson(MockSendResponse.sendMessage("/help", 12345l));
        final SendRequest sendRequest = SendRequest.sendMessage(12345l, new MarkdownStringBuilder().plain("/help"));
        final Send send = Send.of(sendRequest, null);
        assert send.getStatus() == REQUEST;

        // when
        send.sending(fakeSend);

        // then
        final SendResult result = send.result();
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        assertThat(result.getErrorMessage()).isNull();
        assertThat(result.getRequest().getChatId()).isEqualTo(12345l);
        assertThat(result.getRequest().getMessage().toString()).isEqualTo(new MarkdownStringBuilder().plain("/help").toString());
    }

    @DisplayName("응답값(SendResponse)의 정상처리 여부, SendType = document")
    @Test
    void document(){
        // given
        fakeSend.setMockDocumentResponseJson(MockSendResponse.sendDocument(12345l));
        assert fakeSend.isDocumentCalled() == false;
        final SendRequest sendRequest = SendRequest.sendDocument(12345l, "fake document", new MarkdownStringBuilder().plain("fake text"));
        final Send send = Send.of(sendRequest, null);
        assert send.getStatus() == REQUEST;

        // when
        send.sending(fakeSend);

        // then
        assertThat(fakeSend.isDocumentCalled()).isTrue();

        final SendResult result = send.result();
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        assertThat(result.getErrorMessage()).isNull();
        assertThat(result.getRequest().getChatId()).isEqualTo(12345l);
        assertThat(result.getRequest().getMessage().toString()).isEqualTo(new MarkdownStringBuilder().plain("fake text").toString());
    }

    // SendType이 DOCUMENT가 아닐 경우 언제나 MESSAGE로 전달한다.
    @DisplayName("SendType이 명시되지 않으면, 언제나 MESSAGE 타입으로 텔래그램과 통신한다.")
    @Test
    void non_sendType(){
        // given
        fakeSend.setMockMessageResponseJson(MockSendResponse.sendMessage("/help", 12345l));
        assert !fakeSend.isDocumentCalled();
        assert !fakeSend.isMessageCalled();

        // SendType을 명시하지 않는다.
        final SendRequest sendRequest = SendRequest.send(12345l, null, new MarkdownStringBuilder().plain("/help"), null);
        final Send send = Send.of(sendRequest, null);
        assert send.getStatus() == REQUEST;

        // when
        send.sending(fakeSend);

        // then
        assertThat(fakeSend.isDocumentCalled()).isFalse();
        assertThat(fakeSend.isMessageCalled()).isTrue();
    }
}

