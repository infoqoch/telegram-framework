package infoqoch.dictionarybot.send;

// 텔레그램에 메시지를 전달할 때, 정상적으로 데이터를 전달하고, 응답받은 데이터를 정상적으로 처리하는지 확인한다.
// 특히 TelegramSend를 touch 하는지 여부를 검토한다.
public class SendLogSendingTest {
//    FakeTelegramSend fakeSend;
//
//    @BeforeEach
//    void setUp(){
//        fakeSend = new FakeTelegramSend();
//    }
//
//    @DisplayName("응답값(SendResponse)의 정상처리 여부")
//    @Test
//    void message(){
//        // given
//        fakeSend.setMockMessageResponseJson(MockSendResponse.sendMessage("/help", 12345l));
//        final SendRequest sendRequest = SendRequest.sendMessage(12345l, new MarkdownStringBuilder().plain("/help"));
//        final SendLog sendLog = SendLog.of(sendRequest, null);
//        assert sendLog.getStatus() == REQUEST;
//
//        // when
//        sendLog.sending(fakeSend);
//
//        // then
//        final SendResult result = sendLog.result();
//        assertThat(result.getStatus()).isEqualTo(SUCCESS);
//        assertThat(result.getErrorMessage()).isNull();
//        assertThat(result.getRequest().getChatId()).isEqualTo(12345l);
//        assertThat(result.getRequest().getMessage().toString()).isEqualTo(new MarkdownStringBuilder().plain("/help").toString());
//    }
//
//    @DisplayName("응답값(SendResponse)의 정상처리 여부, SendType = document")
//    @Test
//    void document(){
//        // given
//        fakeSend.setMockDocumentResponseJson(MockSendResponse.sendDocument(12345l));
//        assert fakeSend.isDocumentCalled() == false;
//        final SendRequest sendRequest = SendRequest.sendDocument(12345l, "fake document", new MarkdownStringBuilder().plain("fake text"));
//        final SendLog sendLog = SendLog.of(sendRequest, null);
//        assert sendLog.getStatus() == REQUEST;
//
//        // when
//        sendLog.sending(fakeSend);
//
//        // then
//        assertThat(fakeSend.isDocumentCalled()).isTrue();
//
//        final SendResult result = sendLog.result();
//        assertThat(result.getStatus()).isEqualTo(SUCCESS);
//        assertThat(result.getErrorMessage()).isNull();
//        assertThat(result.getRequest().getChatId()).isEqualTo(12345l);
//        assertThat(result.getRequest().getMessage().toString()).isEqualTo(new MarkdownStringBuilder().plain("fake text").toString());
//    }
//
//    // SendType이 DOCUMENT가 아닐 경우 언제나 MESSAGE로 전달한다.
//    @DisplayName("SendType이 명시되지 않으면, 언제나 MESSAGE 타입으로 텔래그램과 통신한다.")
//    @Test
//    void non_sendType(){
//        // given
//        fakeSend.setMockMessageResponseJson(MockSendResponse.sendMessage("/help", 12345l));
//        assert !fakeSend.isDocumentCalled();
//        assert !fakeSend.isMessageCalled();
//
//        // SendType을 명시하지 않는다.
//        final SendRequest sendRequest = SendRequest.send(12345l, null, new MarkdownStringBuilder().plain("/help"), null);
//        final SendLog sendLog = SendLog.of(sendRequest, null);
//        assert sendLog.getStatus() == REQUEST;
//
//        // when
//        sendLog.sending(fakeSend);
//
//        // then
//        assertThat(fakeSend.isDocumentCalled()).isFalse();
//        assertThat(fakeSend.isMessageCalled()).isTrue();
//    }
}

