package infoqoch.telegram.framework.update.send;

import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.Executors;

import static infoqoch.telegram.framework.update.response.SendType.SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
public class SendRequestEventListener {
    private final TelegramSend telegramSend;

    @Async
    @SneakyThrows
    @EventListener(Send.class)
    public void handle(Send send) {
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
    }
}