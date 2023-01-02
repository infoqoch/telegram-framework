package infoqoch.telegram.framework.update.send;

import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

import static infoqoch.telegram.framework.update.response.ResponseType.SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
public class SendEventListener {
    private final TelegramSend telegramSend;

    @Async
    @Transactional
    @EventListener(Send.class)
    public CompletableFuture<SendResult> handle(Send send) {
        try {
            send.sending(telegramSend);
        } catch (Exception e) {
            try {
                log.error("error - SendEventListener, ", e);
                send.resending(SERVER_ERROR, new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (2)"), telegramSend);
            } catch (Exception ee) {
                log.error("error again - SendEventListener, ", ee);
            }
        }
        return CompletableFuture.completedFuture(new SendResult(send));
    }
}