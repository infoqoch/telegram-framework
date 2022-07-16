package infoqoch.dictionarybot.integration.dictionarybot;

import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.repository.SendRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Data
@Profile({"fake_send_listener"})
@Primary
@Component
@RequiredArgsConstructor
public class FakeSendRequestEventListener {
	private final SendRepository store;

    private boolean called = false;
    private Send savedSend;

    @EventListener(Send.class)
    public void handle(Send send) {
        System.out.println("== FakeSendRequestEventListener CALLED!! === ");
        called = true;
        savedSend = store.save(send);
    }
}
