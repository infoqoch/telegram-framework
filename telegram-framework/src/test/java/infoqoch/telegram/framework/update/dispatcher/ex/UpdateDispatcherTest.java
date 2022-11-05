package infoqoch.telegram.framework.update.dispatcher.ex;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import infoqoch.telegram.framework.update.UpdateDispatcher;
import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.response.SendType;
import infoqoch.telegram.framework.update.response.UpdateResponse;
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
class UpdateDispatcherTest {

    private UpdateDispatcher updateDispatcher;

    @BeforeEach
    void setUp(){
        final ApplicationContext ac = new AnnotationConfigApplicationContext(InitConfig.class);
        updateDispatcher = ac.getBean(UpdateDispatcher.class);
    }

    @Test
    void wrong_request_input_null(){
        final UpdateResponse updateResponse = updateDispatcher.process(null);
        assertThat(updateResponse.getSendType()).isEqualTo(SendType.SERVER_ERROR);
        assertThat(updateResponse.getMessage().toString()).isEqualTo("서버에 문제가 발생하였습니다\\. 죄송합니다\\. \\(2\\)");
    }

    @Test
    void wrong_request_input_empty(){
        final UpdateResponse updateResponse = updateDispatcher.process(new UpdateRequest(null));
        assertThat(updateResponse.getSendType()).isEqualTo(SendType.SERVER_ERROR);
        assertThat(updateResponse.getMessage().toString()).isEqualTo("서버에 문제가 발생하였습니다\\. 죄송합니다\\. \\(2\\)");
    }


    @EnableTelegramFramework
    @Configuration
    static class InitConfig {
        @Bean
        public SampleHandler sampleHandler(){
            return new SampleHandler();
        }
    }
}