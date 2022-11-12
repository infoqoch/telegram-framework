package infoqoch.telegram.framework.update.send;

import infoqoch.telegram.framework.update.exception.TelegramServerException;
import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static infoqoch.telegram.framework.update.response.ResponseType.SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
public class SendUpdateResponseEventListener {
    private final TelegramSend telegramSend;

    @Async
    @Transactional
    @EventListener(Send.class)
    public void handle(Send send) {
        try {
            Executors.newCachedThreadPool().submit(() -> {
                try {
                    send.sending(telegramSend);
                } catch (Exception e) {
                    log.error("error - SendRequestEventListener, ", e);
                    try {
                        send.resending(SERVER_ERROR, new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (2)"), telegramSend);
                    } catch (Exception ee) {
                        log.error("error again - SendRequestEventListener, ", ee);
                    }
                }
                send.done();
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new TelegramServerException("SendRequestEventListener#handle이 메시지 전송 중 예외 발생", e);
        }
    }
}