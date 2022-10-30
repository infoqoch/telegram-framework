package infoqoch.dictionarybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DictionaryBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(DictionaryBotApplication.class, args);
    }
}
