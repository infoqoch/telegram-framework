package infoqoch.telegram.framework.update.dispatcher.mapper.simillar;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import infoqoch.telegram.framework.update.UpdateDispatcher;
import infoqoch.telegram.framework.update.mock.MockUpdate;
import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import infoqoch.telegrambot.bot.entity.Update;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class SimilarUpdateDispatcherTest {

    private UpdateDispatcher updateDispatcher;

    @BeforeEach
    void setUp(){
        final ApplicationContext ac = new AnnotationConfigApplicationContext(InitConfig.class);

        final SampleHandler bean = ac.getBean(SampleHandler.class);
        assert bean != null;

        updateDispatcher = ac.getBean(UpdateDispatcher.class);
    }

    @Test
    @DisplayName("명령어를 필터링할 때 중복된 결과가 나올 경우, 그 중 가장 짧은 명령어를 가진 리졸버를 선택한다.")
    void determine_longest_resolver() {
        // help 동작
        assertDispatcherWithMessage("/help", new MarkdownStringBuilder("help!").toString());
        assertDispatcherWithMessage("/help m", new MarkdownStringBuilder("help!").toString());
        assertDispatcherWithMessage("/help mee", new MarkdownStringBuilder("help!").toString());

        // help me 동작
        assertDispatcherWithMessage("/help me", new MarkdownStringBuilder("help ME!!").toString());
        assertDispatcherWithMessage("/help me everything!", new MarkdownStringBuilder("help ME!!").toString());
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