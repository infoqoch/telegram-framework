package infoqoch.dictionarybot.update.exception;

import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// TODO
// Exception의 전파에 대한 테스트를 진행하였음.
// 현재로서는 필요 없어 보임. 검토 후 삭제여부 결정.
@Deprecated
class TelegramExceptionTest {
    @Test
    void instanceOf(){
        TelegramClientException telegramClientException = new TelegramClientException();
        System.out.println("(telegramClientException instanceof TelegramException) = " + (telegramClientException instanceof TelegramException));
    }

    @Test
    void cause_by_it(){
        boolean isCatch = false;
        try{
            outer();
        }catch (Exception e){
            isCatch = true;
            final Optional<TelegramException> telegramException = TelegramException.checkIfCausedByTelegramException(e);
            assertThat(telegramException).isPresent();
        }
        assertThat(isCatch).isTrue();
    }

    void outer(){
        try{
            inner();
        }catch (Exception e){
            throw new IllegalArgumentException(e);
        }


    }

    void inner(){
        throw new TelegramClientException(new MarkdownStringBuilder("사용자 전달 메시지"), "서버 내부 메시지");
    }

}