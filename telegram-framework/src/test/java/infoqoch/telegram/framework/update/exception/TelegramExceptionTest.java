package infoqoch.telegram.framework.update.exception;

import infoqoch.telegram.framework.update.response.ResponseType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TelegramExceptionTest {

    @Test
    void propagated_errors(){
        Optional<TelegramException> op = Optional.empty();
        try{
            propagated();
        }catch (Exception e){
            op = TelegramException.checkIfCausedByTelegramException(e);
        }
        assertThat(op).isPresent();
        assertThat(op.get().resolveErrorType()).isEqualTo(ResponseType.CLIENT_ERROR);
    }

    @Test
    void client(){
        // given
        final Throwable ex = new TelegramClientException(new MarkdownStringBuilder("사용자 전달 메시지"), "서버 내부 메시지");

        // when
        final Optional<TelegramException> result = TelegramException.checkIfCausedByTelegramException(ex);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().resolveErrorType()).isEqualTo(ResponseType.CLIENT_ERROR);
        assertThat(result.get().response().get().toString()).isEqualTo(new MarkdownStringBuilder("사용자 전달 메시지").toString());
    }

    @Test
    void server(){
        // given
        final Throwable ex = new TelegramServerException(new MarkdownStringBuilder("사용자 전달 메시지"), "AbcService 에서 에러 발생!" ,new IllegalStateException());

        // when
        final Optional<TelegramException> result = TelegramException.checkIfCausedByTelegramException(ex);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().resolveErrorType()).isEqualTo(ResponseType.SERVER_ERROR);
        assertThat(result.get().response().get().toString()).isEqualTo(new MarkdownStringBuilder("사용자 전달 메시지").toString());
    }

    void propagated(){
        try{
            causedByTelegramException();
        }catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }

    void causedByTelegramException(){
        throw new TelegramClientException(new MarkdownStringBuilder("사용자 전달 메시지"), "서버 내부 메시지");
    }
}