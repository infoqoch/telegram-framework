package infoqoch.dictionarybot.main;

import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.SendRequest;
import infoqoch.dictionarybot.send.service.SendRunnerService;
import infoqoch.dictionarybot.system.event.Events;
import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static infoqoch.dictionarybot.send.SendType.SERVER_ERROR;

@Async
@Slf4j
@RequiredArgsConstructor
public class DictionarySendRunner {
    private final TelegramSend telegramSend;
    private final SendRunnerService sendRunnerService;

    @Scheduled(fixedDelay = 100)
    @Transactional
    public void run() {
        List<Send> sendRequests = sendRunnerService.findByStatusForScheduler(Send.Status.REQUEST);

        for (Send send : sendRequests) {
            try{
                send.sending(telegramSend);
            }catch (Exception e){
                exceptionHandler(e, send);
                continue;
            }
        }
    }

    private void exceptionHandler(Exception e, Send send) {
        log.error("[error : {}], ", "DictionarySendRunner", e);
        final SendRequest sendRequest = SendRequest.send(send.getRequest().getChatId(), SERVER_ERROR, new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (2)"), null);
        Events.raise(Send.of(sendRequest));
    }
}
