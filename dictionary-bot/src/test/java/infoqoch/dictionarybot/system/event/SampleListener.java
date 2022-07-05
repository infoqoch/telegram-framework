package infoqoch.dictionarybot.system.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SampleListener {

    @EventListener(Sample.class)
    public void handle(Sample sample) {
        System.out.println("SampleListener#handler called!");
        sample.setCalled(true);
    }
}
