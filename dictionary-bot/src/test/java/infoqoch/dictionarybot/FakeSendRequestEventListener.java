package infoqoch.dictionarybot;

import infoqoch.dictionarybot.log.send.Send;
import infoqoch.dictionarybot.log.send.repository.SendRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Data
@Profile("fake_send_listener")
@Primary
@Component
@Transactional
@RequiredArgsConstructor
public class FakeSendRequestEventListener {
	private final SendRepository sendRepository;

    private boolean called = false;
    private Send latestSent;
    private List<Send> sentList = new ArrayList<>();

    @EventListener(Send.class)
    public void handle(Send send) {
        System.out.println("== FakeSendRequestEventListener CALLED!! === ");
        called = true;
        latestSent = sendRepository.save(send);
        sentList.add(send);
    }

    public void clear() {
        called = false;
        latestSent = null;
        sentList = new ArrayList<>();
    }
}
