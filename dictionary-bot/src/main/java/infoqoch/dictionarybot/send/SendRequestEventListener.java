package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.send.repository.SendRepository;
import infoqoch.dictionarybot.send.request.SendRequest;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SendRequestEventListener {
	private final SendRepository store;

    @EventListener(SendRequest.class)
    public void handle(SendRequest request) {
        Send send = Send.of(request);
        store.save(send);
    }



}
