package infoqoch.telegram.framework.update.dispatcher.mapper.basic;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import infoqoch.telegram.framework.update.UpdateConfig;
import infoqoch.telegram.framework.update.UpdateDispatcher;
import infoqoch.telegram.framework.update.mock.MockUpdate;
import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import infoqoch.telegrambot.bot.entity.Update;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
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

        final SampleHandler bean = ac.getBean(SampleHandler.class);
        assert bean != null;

        final UpdateConfig updateConfig = new UpdateConfig();
        updateDispatcher = updateConfig.updateDispatcher(ac);
    }

    @Test
    void message(){
        // help 동작
        assertDispatcherWithMessage("/help", new MarkdownStringBuilder("I am going to help you!").toString());
        assertDispatcherWithMessage("_help hi", new MarkdownStringBuilder("I am going to help you!").toString());
        assertDispatcherWithMessage("help", new MarkdownStringBuilder("I am going to help you!").toString());
        assertDispatcherWithMessage("/help_hi", new MarkdownStringBuilder("I am going to help you!").toString());
        assertDispatcherWithMessage("/help 12213 213213", new MarkdownStringBuilder("I am going to help you!").toString());
        assertDispatcherWithMessage("/help hello", new MarkdownStringBuilder("I am going to help you!").toString());

        // hello 동작
        assertDispatcherWithMessage("hello", new MarkdownStringBuilder("hi!").toString());

        // * 동작
        assertDispatcherWithMessage("ewrfiuhj8932jf", new MarkdownStringBuilder("I am going to help you!").toString());
        assertDispatcherWithMessage("1hello help", new MarkdownStringBuilder("I am going to help you!").toString());
        assertDispatcherWithMessage("hello1 help", new MarkdownStringBuilder("I am going to help you!").toString());
        assertDispatcherWithMessage("hello1 help", new MarkdownStringBuilder("I am going to help you!").toString());
        assertDispatcherWithMessage("hel_lo1 help", new MarkdownStringBuilder("I am going to help you!").toString());
        assertDispatcherWithMessage("!hello help", new MarkdownStringBuilder("I am going to help you!").toString());
        assertDispatcherWithMessage("hello! help", new MarkdownStringBuilder("I am going to help you!").toString());
    }

    @Test
    void document() {
        // given
        final UpdateRequest updateRequest = chatMessageToUpdateRequest("docu");

        // when
        final UpdateResponse process = updateDispatcher.process(updateRequest);

        // then
        assertThat(process.getDocument()).isEqualTo("document_as_file_id");
    }

    private UpdateRequest chatMessageToUpdateRequest(String docu) {
        final String chatJson = MockUpdate.chatJson(docu);
        final Update update = MockUpdate.jsonToUpdate(chatJson);
        final UpdateRequest updateRequest = new UpdateRequest(update);
        return updateRequest;
    }

    private void assertDispatcherWithMessage(String input, String output) {
        // given
        final UpdateRequest updateRequest = chatMessageToUpdateRequest(input);

        // when
        final UpdateResponse process = updateDispatcher.process(updateRequest);

        // then
        assertThat(process.getMessage().toString()).isEqualTo(output);
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