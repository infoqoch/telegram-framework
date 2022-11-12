package infoqoch.telegram.framework.update.send;

import infoqoch.telegram.framework.update.mock.MockSendResponse;
import infoqoch.telegram.framework.update.response.ResponseType;
import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.response.SendMessageResponse;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static infoqoch.telegram.framework.update.response.ResponseType.MESSAGE;
import static infoqoch.telegram.framework.update.send.Send.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class SendUpdateResponseEventListenerTest {
    SendUpdateResponseEventListener listener;
    TelegramSend mockTelegramSend;
    final DefaultJsonBind binder = DefaultJsonBind.getInstance();


    @BeforeEach
    void setUp(){
        mockTelegramSend = mock(TelegramSend.class);
        listener = new SendUpdateResponseEventListener(mockTelegramSend);
    }

    @Test
    @DisplayName("정상적으로 요청하고 정상적으로 응답함")
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void message_send_success() {
        // given
        final Response good1 = binder.toObject(MockSendResponse.sendMessage("good", 123l), SendMessageResponse.class);
        given(mockTelegramSend.message(any())).willReturn(good1);

        final Send send = Send.sendMessage(123l, new MarkdownStringBuilder("hello!"));

        // when
        listener.handle(send);

        // then
        while(!send.isDone());
        assertThat(send.getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    @DisplayName("발송 자체에서 에러가 발생함. 서버 에러로 한 번 더 보낸다.")
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void message_send_error() {
        // given
        given(mockTelegramSend.message(any())).willThrow(RuntimeException.class);
        final Send send = Send.sendMessage(123l, new MarkdownStringBuilder("hello!"));

        // when
        listener.handle(send);

        // then
        while(!send.isDone());
        assertThat(send.getResponseType()).isEqualTo(MESSAGE);
        assertThat(send.getStatus()).isEqualTo(ERROR);

        final Optional<Send> resend = send.getResend();
        assertThat(resend).isPresent();
        assertThat(resend.get().getMessage().toString()).isEqualTo(new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (2)").toString());
        assertThat(resend.get().getResponseType()).isEqualTo(ResponseType.SERVER_ERROR);
    }

    @Test
    @DisplayName("잘 발송하였으나 응답이 부정적임")
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void message_send_response_error() {
        // given
        final Response response = binder.toObject("{\"ok\":false,\"error_code\":400,\"description\":\"Bad Request: chat not found\"}", Object.class);
        given(mockTelegramSend.message(any())).willReturn(response);

        final Send send = Send.sendMessage(123l, new MarkdownStringBuilder("hello!"));

        // when
        listener.handle(send);

        // then
        while(!send.isDone());
        assertThat(send.getStatus()).isEqualTo(RESPONSE_ERROR);
        assertThat(send.getErrorMessage()).isEqualTo("Bad Request: chat not found");
    }
}