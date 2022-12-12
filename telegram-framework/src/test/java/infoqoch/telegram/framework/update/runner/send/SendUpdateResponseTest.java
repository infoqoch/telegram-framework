package infoqoch.telegram.framework.update.runner.send;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import infoqoch.telegram.framework.update.TelegramProperties;
import infoqoch.telegram.framework.update.UpdateRequestMapper;
import infoqoch.telegram.framework.update.UpdateRunner;
import infoqoch.telegram.framework.update.mock.MockUpdate;
import infoqoch.telegram.framework.update.runner.bot.FakeTelegramBot;
import infoqoch.telegram.framework.update.runner.bot.FakeTelegramSend;
import infoqoch.telegram.framework.update.runner.bot.FakeTelegramUpdate;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SendUpdateResponseTest {
    UpdateRunner runner;
    FakeTelegramBot telegramBot;
    FakeTelegramUpdate update;
    FakeTelegramSend send;

    @BeforeEach
    void setUp() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(Config.class);

        // FakeTelegramBot을 빈으로 등록한다.
        TelegramBot telegramBot = ac.getBean(TelegramBot.class);
        assert telegramBot instanceof FakeTelegramBot;
        this.telegramBot = (FakeTelegramBot) telegramBot;
        update = (FakeTelegramUpdate) this.telegramBot.update();
        send = (FakeTelegramSend) this.telegramBot.send();

        runner = ac.getBean(UpdateRunner.class);
    }

    // 중요!
    // FakeTelegramSend가 완전하게 정의되지 않아 메시지가 두 번 호출 됨(정상호출 -> 미완성된 구현으로 인한 에러 대응 메시지 호출)
    // 이벤트 리스너의 정상동작을 확인하는 것이 우선이기 때문에 추가적인 구현은 생략한다.
    @Test
    void success(){
        // given
        update.mock = MockUpdate.responseWithSingleChat("/help hihi", 123l);

        // when
        runner.run();

        // then
        final List<SendMessageRequest> messageRequests = send.messageRequests;
        assertThat(messageRequests).size().isGreaterThanOrEqualTo(1);
    }

    public static class SampleHandler {
        @UpdateRequestMapper({"help", "*"})
        public String help(){
            return "I am going to help you!";
        }

        @UpdateRequestMapper("voids")
        public void voids() {
            System.out.println("hi!");
        }
    }

    @Configuration
    @EnableTelegramFramework
    static class Config{
        @Primary
        @Bean
        TelegramBot telegramBot(){
            return new FakeTelegramBot(new FakeTelegramUpdate(), new FakeTelegramSend());
        }

        @Primary
        @Bean
        TelegramProperties telegramProperties(){
            return TelegramProperties.generate("telegram-framework.properties");
        }

        @Bean
        SampleHandler sampleHandler(){
            return new SampleHandler();
        }
    }
}