package infoqoch.telegramframework.spring.simpleinspring;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableTelegramFramework
@EnableScheduling
@EnableAsync

@SpringBootApplication
public class SimpleInSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleInSpringApplication.class, args);
    }

}
