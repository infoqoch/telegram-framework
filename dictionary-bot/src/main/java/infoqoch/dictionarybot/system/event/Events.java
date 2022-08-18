package infoqoch.dictionarybot.system.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
public class Events {
	private static ApplicationEventPublisher publisher;

	static void setPublisher(ApplicationEventPublisher publisher) {
		Events.publisher = publisher;
	}

	public static void raise (Object event) {
		if (publisher != null) {
			publisher.publishEvent(event);
		}else{
			log.error("EventPublisher not injected");
		}
	}

}
