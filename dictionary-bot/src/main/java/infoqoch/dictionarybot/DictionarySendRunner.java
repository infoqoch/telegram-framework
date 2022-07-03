package infoqoch.dictionarybot;

import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.SendDispatcher;
import infoqoch.dictionarybot.send.repository.SendRepository;
import infoqoch.dictionarybot.send.response.SendResponse;
import infoqoch.dictionarybot.system.exception.TelegramErrorResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DictionarySendRunner {
    private final SendDispatcher sendDispatcher;
    private final SendRepository sendRepository;

    @Scheduled(fixedDelay = 500)
    public void run() {
        List<Send> sendRequests = sendRepository.findByStatus(Send.Status.REQUEST);

        for (Send send : sendRequests) {
            send.sending();

            try {
                SendResponse sendResponse = sendDispatcher.process(send.getRequest());
                send.success(sendResponse);
            } catch (TelegramErrorResponseException e){
                log.error("[error : {}], ", "DictionarySendRunner", e);
                send.responseError(e);
            }catch (Exception e) {
                log.error("[error : {}], ", "DictionarySendRunner", e);
                send.error(e);
            }
        }
    }
}
