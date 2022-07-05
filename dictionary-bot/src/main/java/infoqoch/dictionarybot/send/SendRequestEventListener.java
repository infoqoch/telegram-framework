package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.send.repository.SendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendRequestEventListener {
	private final SendRepository store;

    @EventListener(Send.class)
    public void handle(Send send) {
        store.save(send);
    }
}
