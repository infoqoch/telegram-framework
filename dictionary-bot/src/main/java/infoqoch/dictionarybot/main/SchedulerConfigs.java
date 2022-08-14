package infoqoch.dictionarybot.main;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Profile("scheduler")
@EnableScheduling
@Configuration
public class SchedulerConfigs {
}
