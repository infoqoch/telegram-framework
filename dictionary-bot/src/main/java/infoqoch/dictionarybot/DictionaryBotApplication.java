package infoqoch.dictionarybot;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableTelegramFramework
@EnableAsync

@SpringBootApplication
@EnableJpaAuditing
public class DictionaryBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(DictionaryBotApplication.class, args);
    }
}
