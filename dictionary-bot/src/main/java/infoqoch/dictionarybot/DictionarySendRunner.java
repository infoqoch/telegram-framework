package infoqoch.dictionarybot;

import infoqoch.dictionarybot.send.SendDispatcher;
import infoqoch.dictionarybot.send.request.SendRequest;
import infoqoch.dictionarybot.send.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DictionarySendRunner {
    private final SendDispatcher sendDispatcher;

    public DictionarySendRunner(SendDispatcher sendDispatcher) {
        this.sendDispatcher = sendDispatcher;
    }

    @Scheduled(fixedDelay = 500)
    public void run() {
        List<SendRequest> sendRequests = new ArrayList<>();

        for (SendRequest sendRequest : sendRequests) {
            resolveSend(sendRequest);
        }
    }

    private SendResponse resolveSend(SendRequest request) {
        try{
            return sendDispatcher.process(request);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
