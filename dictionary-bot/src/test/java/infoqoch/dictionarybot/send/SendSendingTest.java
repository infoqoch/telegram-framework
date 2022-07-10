package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.mock.data.MockSendResponse;
import infoqoch.dictionarybot.run.FakeTelegramSend;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static infoqoch.dictionarybot.send.Send.Status.REQUEST;
import static infoqoch.dictionarybot.send.Send.Status.SUCCESS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SendSendingTest {
    FakeTelegramSend fakeSend;

    @BeforeEach
    void setUp(){
        fakeSend = new FakeTelegramSend();
    }

    @DisplayName("가상의 응답값(SendResponse)의 정상처리 여부")
    @Test
    void message(){
        // given
        fakeSend.setMockMessageResponseJson(MockSendResponse.sendMessage("/help", 12345l));
        final SendRequest sendRequest = SendRequest.requestMessage(12345l, new MarkdownStringBuilder().plain("/help"));
        final Send send = Send.of(sendRequest, null);
        assert send.status() == REQUEST;

        // when
        send.sending(fakeSend);

        // then
        final SendResult result = send.result();
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        assertThat(result.getErrorMessage()).isNull();
        assertThat(result.getRequest().chatId()).isEqualTo(12345l);
        assertThat(result.getRequest().message().toString()).isEqualTo(new MarkdownStringBuilder().plain("/help").toString());
    }

    @DisplayName("가상의 응답값(SendResponse)의 정상처리 여부, sendtype = document")
    @Test
    void document(){
        // given
        fakeSend.setMockDocumentResponseJson(MockSendResponse.sendDocument(12345l));
        assert fakeSend.isDocumentCalled() == false;
        final SendRequest sendRequest = SendRequest.sendDocument(12345l, "fake document", new MarkdownStringBuilder().plain("fake text"));
        final Send send = Send.of(sendRequest, null);
        assert send.status() == REQUEST;

        // when
        send.sending(fakeSend);

        // then
        assertThat(fakeSend.isDocumentCalled()).isTrue();

        final SendResult result = send.result();
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        assertThat(result.getErrorMessage()).isNull();
        assertThat(result.getRequest().chatId()).isEqualTo(12345l);
        assertThat(result.getRequest().message().toString()).isEqualTo(new MarkdownStringBuilder().plain("fake text").toString());
    }
}
