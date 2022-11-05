package infoqoch.telegram.framework.update.dispatcher.ex;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import infoqoch.telegram.framework.update.UpdateDispatcher;
import infoqoch.telegram.framework.update.mock.MockUpdate;
import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.response.SendType;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @Test
    @DisplayName("예외를 클라이언트에 응답하는 메시지(MSB)가 존재한다. 이 경우 TelegramException으로 예외를 발생시킨다.")
    void wrong_request_input_telegramEx(){
        // given
        final String notSpecificCommand = "wmiofewjmf";
        final UpdateRequest request = MockUpdate.jsonToUpdateRequest(MockUpdate.chatJson(notSpecificCommand));

        // when
        final UpdateResponse updateResponse = updateDispatcher.process(request);

        // then
        assertThat(updateResponse.getSendType()).isEqualTo(SendType.CLIENT_ERROR);
        assertThat(updateResponse.getMessage().toString()).isEqualTo(new MarkdownStringBuilder("정확한 명령어를 입력해야 합니다.").toString());
    }

    @Test
    @DisplayName("예외를 클라이언트에 응답하는 메시지(MSB)가 없는 경우, default message가 전달된다.")
    void wrong_request_input_telegramEx_no_response(){
        // given
        final String noReseponseExceptionCommand = "/noresponse hi!";
        final UpdateRequest request = MockUpdate.jsonToUpdateRequest(MockUpdate.chatJson(noReseponseExceptionCommand));

        // when
        final UpdateResponse updateResponse = updateDispatcher.process(request);

        // then
        assertThat(updateResponse.getSendType()).isEqualTo(SendType.SERVER_ERROR);
        assertThat(updateResponse.getMessage().toString()).isEqualTo(new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (2)").toString());
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