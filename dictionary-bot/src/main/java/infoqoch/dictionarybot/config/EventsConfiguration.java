package infoqoch.dictionarybot.config;

import infoqoch.dictionarybot.system.event.Events;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class EventsConfiguration {
	private final ApplicationContext applicationContext;

	@Bean
	public InitializingBean eventsInitializer() {
		return () -> Events.setPublisher(applicationContext);
	}

}
