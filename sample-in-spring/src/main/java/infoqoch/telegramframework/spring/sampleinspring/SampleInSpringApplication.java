package infoqoch.telegramframework.spring.sampleinspring;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

// telegram-framework 를 사용하기 위해서는 @EnableTelegramFramework을 반드시 필요로 한다.
@EnableTelegramFramework

// update - send의 실시간 처리를 위한 UpdateRunner 는 스케줄러를 기반으로 작성되었다.
// UpdateRunner 는 동기적으로 동작한다.
// UpdateRunner 를 사용하지 않고자 한다면 UpdateDispatcher 빈을 직접 다룬다. @UpdateRequestMapper 에 대한 해석 및 응답값 제공까지만 지원한다.
@EnableScheduling

@EnableAsync
@SpringBootApplication
public class SampleInSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleInSpringApplication.class, args);
    }

}
