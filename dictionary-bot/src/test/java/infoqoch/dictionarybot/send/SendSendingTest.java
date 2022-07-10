package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.mock.data.MockSendResponse;
import infoqoch.dictionarybot.run.FakeTelegramSend;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        // given. sendRequest를 telegram에 보내고 telegram의 정상 응답값을 받았음. 해당 응답값에 대한 대역
        fakeSend.setMockMessageResponseJson(MockSendResponse.sendMessage("/help", 12345l));
        final SendRequest sendRequest = new SendRequest(12345l, SendType.MESSAGE, new MarkdownStringBuilder().plain("/help"));

        // when
        final Send send = Send.of(sendRequest, null);
        send.sending(fakeSend);

        // then
        assertThat(send.status()).isEqualTo(SUCCESS);
        // TODO
        // 어떤 수준의 데이터를 SendResult로 전달할지 고민이 필요.
//        assertThat(result.getChat().getId()).isEqualTo(12345l);
//        assertThat(result.getText()).isEqualTo("/help");
    }

    @DisplayName("가상의 응답값(SendResponse)의 정상처리 여부, sendtype = document")
    @Test
    void document(){
        // given
        fakeSend.setMockDocumentResponseJson(MockSendResponse.sendDocument(12345l));
        assert fakeSend.isDocumentCalled() == false;
        final SendRequest sendRequest = new SendRequest(12345l, SendType.DOCUMENT, "fake document", new MarkdownStringBuilder().plain("fake text"));

        // when
        final Send send = Send.of(sendRequest, null);
        send.sending(fakeSend);

        // then
        assertThat(fakeSend.isDocumentCalled()).isTrue();

        assertThat(send.status()).isEqualTo(SUCCESS);
    }
}
