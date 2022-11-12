package infoqoch.telegram.framework.update.runner;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import infoqoch.telegram.framework.update.UpdateRequestMethodMapper;
import infoqoch.telegram.framework.update.UpdateRunner;
import infoqoch.telegram.framework.update.mock.MockUpdate;
import infoqoch.telegram.framework.update.runner.bot.FakeTelegramBot;
import infoqoch.telegram.framework.update.runner.bot.FakeTelegramSend;
import infoqoch.telegram.framework.update.runner.bot.FakeTelegramUpdate;
import infoqoch.telegram.framework.update.send.Send;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateRunnerTest {
    UpdateRunner runner;
    FakeTelegramBot telegramBot;
    FakeTelegramUpdate update;
    SendSubscribeEventListener listener;

    @BeforeEach
    void setUp() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(Config.class);

        // FakeTelegramBot을 빈으로 등록한다.
        TelegramBot telegramBot = ac.getBean(TelegramBot.class);
        assert telegramBot instanceof FakeTelegramBot;
        this.telegramBot = (FakeTelegramBot) telegramBot;
        update = (FakeTelegramUpdate) this.telegramBot.update();

        runner = ac.getBean(UpdateRunner.class);
        listener = ac.getBean(SendSubscribeEventListener.class);
    }

    @Test
    void success(){
        // given
        update.mock = MockUpdate.responseWithSingleChat("/help hihi", 123l);

        // when
        runner.run();

        // then
        final List<Send> list = listener.list;
        assertThat(list).size().isEqualTo(1);
        assertThat(list.get(0).getUpdateRequest().get().updateRequestCommandAndValue().getCommand().get()).isEqualTo("help");
        assertThat(list.get(0).getUpdateRequest().get().updateRequestCommandAndValue().getValue()).isEqualTo("hihi");
        assertThat(list.get(0).getUpdateResponse().get().getMessage().toString()).isEqualTo(new MarkdownStringBuilder("I am going to help you!").toString());
    }

    public static class SendSubscribeEventListener{
        public List<Send> list = new ArrayList<>();

        @EventListener(Send.class)
        public void handle(Send send) {
            list.add(send);
        }
    }

    public static class SampleHandler {
        @UpdateRequestMethodMapper({"help", "*"})
        public String help(){
            return "I am going to help you!";
        }

        @UpdateRequestMethodMapper("voids")
        public void voids() {
            System.out.println("hi!");
        }
    }

    @Configuration
    @ComponentScan
    @EnableTelegramFramework
    static class Config{

        @Primary
        @Bean
        TelegramBot telegramBot(){
            return new FakeTelegramBot(new FakeTelegramUpdate(), new FakeTelegramSend());
        }

        @Bean
        SampleHandler sampleHandler(){
            return new SampleHandler();
        }

        @Bean
        SendSubscribeEventListener sendSubscribeEventListener(){
            return new SendSubscribeEventListener();
        }
    }
}