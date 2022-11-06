package infoqoch.dictionarybot.send;

import infoqoch.mock.bot.FakeTelegramBot;
import infoqoch.mock.bot.FakeTelegramSend;
import infoqoch.mock.data.MockSendResponse;
import infoqoch.mock.repository.MemorySendRepository;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static infoqoch.dictionarybot.send.Send.Status.*;
import static org.assertj.core.api.Assertions.assertThat;

class SendRequestEventListenerTest {
    SendRequestEventListener listener;
    MemorySendRepository repository;
    FakeTelegramSend telegramSend;

    @BeforeEach
    void setUp(){
        repository = new MemorySendRepository();
        telegramSend = new FakeTelegramSend();
        listener = new SendRequestEventListener(repository, new FakeTelegramBot(telegramSend));
    }

    @Test
    @DisplayName("정상적으로 요청하고 정상적으로 응답함")
    void message_send_success() {
        // given
        telegramSend.setMockMessageResponseJson(MockSendResponse.sendMessage("/w_hi", 1235l));
        final Send send = new Send(1l, SendRequest.sendMessage(1235l, new MarkdownStringBuilder().plain("hi의 검색결과는 다음과 같습니다")), Send.Status.REQUEST);

        // when
        listener.handle(send);

        // then
        assertThat(send.getStatus()).isEqualTo(SUCCESS);

        final List<Send> sends = repository.findByStatus(SUCCESS);
        assertThat(sends).size().isEqualTo(1);
        assertThat(sends.get(0).result().getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    @DisplayName("발송 자체에서 에러가 발생함. 서버 에러로 한 번 더 보낸다.")
    void message_send_error() {
        // given
        telegramSend.setThrowRuntimeException(true); // 데이터 전송 과정에서 예외가 발생하였음.
        final Send send = new Send(1l, SendRequest.sendMessage(324893249234l, new MarkdownStringBuilder().plain("hi의 검색결과는 다음과 같습니다")), Send.Status.REQUEST);

        // when
        // 예외가 두 번 발생함. 왜냐하면 TelegramSend를 접근하면 무조건 두 번 예외를 발생하기 때문에
        // 흐름은...
        // 응답 메시지 생성 -> Listener가 TelegramSend로 요청 -> TelegramSend에서 예외 발생 -> Listener가 예외를 잡음 -> 실패하였다고 TelegramSend에 재요청 -> 재요청을 다시 실패
        // 결과적으로 Send는 두 개가 생성된다.
        try{
            listener.handle(send);
        }catch (Exception e){

        }


        // then
        assertThat(send.getStatus()).isEqualTo(ERROR);

        final List<Send> sends = repository.findByStatus(ERROR);
        assertThat(sends).size().isEqualTo(2);
        assertThat(sends.get(0).result().getStatus()).isEqualTo(ERROR);
    }

    @Test
    @DisplayName("잘 발송하였으나 응답이 부정적임")
    void message_send_response_error() {
        // given
        telegramSend.setMockMessageResponseJson("{\"ok\":false,\"error_code\":400,\"description\":\"Bad Request: chat not found\"}"); // 데이터는 잘 전송하였으나, 텔레그램에서 bad request로 응답하였음.
        final Send send = new Send(1l, SendRequest.sendMessage(324893249234l, new MarkdownStringBuilder().plain("hi의 검색결과는 다음과 같습니다")), Send.Status.REQUEST);

        // whend
        listener.handle(send);

        // then
        assertThat(send.getStatus()).isEqualTo(RESPONSE_ERROR);
         final List<Send> sends = repository.findByStatus(RESPONSE_ERROR);
        assertThat(sends).size().isEqualTo(1);
        assertThat(sends.get(0).result().getErrorMessage()).isEqualTo("Bad Request: chat not found");
        assertThat(sends.get(0).getStatus()).isEqualTo(RESPONSE_ERROR);
    }
}