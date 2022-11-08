package infoqoch.telegram.framework.update.dispatcher.mapper.voids;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import infoqoch.telegram.framework.update.UpdateDispatcher;
import infoqoch.telegram.framework.update.mock.MockUpdate;
import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.response.SendType;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import infoqoch.telegrambot.bot.entity.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

// UpdateDispatcher 의 목표는
// @UpdateRequestMethodMapper 의 명령어를 기준으로 적합한 리턴 값을 전달하는 것
// 기본적인 동작...
// 리팩터링의 기준이 된다.
class VoidsUpdateDispatcherTest {

    private UpdateDispatcher updateDispatcher;

    @BeforeEach
    void setUp(){
        final ApplicationContext ac = new AnnotationConfigApplicationContext(InitConfig.class);

        final VoidsHandler bean = ac.getBean(VoidsHandler.class);
        assert bean != null;

        updateDispatcher = ac.getBean(UpdateDispatcher.class);
    }

    @Test
    void message(){
        // help 동작
        // given
        final UpdateRequest updateRequest = chatMessageToUpdateRequest("/voids");

        // when
        final UpdateResponse process = updateDispatcher.process(updateRequest);

        // then
        assertThat(process.getSendType()).isEqualTo(SendType.VOID);
    }

    private UpdateRequest chatMessageToUpdateRequest(String docu) {
        final String chatJson = MockUpdate.chatJson(docu);
        final Update update = MockUpdate.jsonToUpdate(chatJson);
        final UpdateRequest updateRequest = new UpdateRequest(update);
        return updateRequest;
    }

    @EnableTelegramFramework
    @Configuration
    static class InitConfig {
        @Bean
        public VoidsHandler sampleHandler(){
            return new VoidsHandler();
        }
    }
}