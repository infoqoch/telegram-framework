package infoqoch.dictionarybot.update;

import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.request.body.MockUpdateJsonGenerate;
import infoqoch.dictionarybot.update.request.body.UpdateChat;
import infoqoch.dictionarybot.update.request.body.UpdateWrapper;
import infoqoch.dictionarybot.update.resolver.mapper.UpdateRequestBodyParameterMapper;
import infoqoch.dictionarybot.update.resolver.mapper.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateDispatcherTest {

    private UpdateDispatcher updateDispatcher;

    @BeforeEach
    void setUp() {
        Map<Class<?>, Object> context = new HashMap<>();
        final Object bean = new TestHandler();
        context.put(bean.getClass(), bean);

        updateDispatcher = new UpdateDispatcher(this.getClass().getPackage().getName(), context);
    }

    @Test
    void resolver_default_signature_test(){
        // given
        UpdateWrapper update = MockUpdateJsonGenerate.toUpdateRequestBody(MockUpdateJsonGenerate.mockDocumentJsonUpdate("/help_hello!"));

        UpdateResponse response = updateDispatcher.process(update);

        // then
        assertThat(response.type()).isEqualTo(SendType.MESSAGE);
        assertThat(response.body()).isInstanceOf(String.class);
        assertThat((String) response.body()).isEqualTo("help! hello!");
    }

    @Test
    void resolver_custom_parameter(){
        // given
        UpdateWrapper update = MockUpdateJsonGenerate.toUpdateRequestBody(MockUpdateJsonGenerate.mockDocumentJsonUpdate("/s_orange"));

        // when
        UpdateResponse response = updateDispatcher.process(update);

        // then
        assertThat(response.type()).isEqualTo(SendType.MESSAGE);
        assertThat(response.body()).isInstanceOf(String.class);
        assertThat((String) response.body()).isEqualTo("LOOKUP_SENTENCE : orange");
    }

    @Test
    void resolver_custom_return(){
        // given
        UpdateWrapper update = MockUpdateJsonGenerate.toUpdateRequestBody(MockUpdateJsonGenerate.mockDocumentJsonUpdate("/w_apple"));

        UpdateResponse response = updateDispatcher.process(update);

        // then
        assertThat(response.type()).isEqualTo(SendType.MESSAGE);
        assertThat(response.body()).isInstanceOf(String.class);
        assertThat((String) response.body()).isEqualTo("LOOKUP_WORD : apple : 2149");
    }

    public static class TestHandler {
        @UpdateRequestMethodMapper(LOOKUP_SENTENCE)
        public String lookupBySentence(UpdateRequest request){
            StringBuilder sb = new StringBuilder();
            return "LOOKUP_SENTENCE : "+ request.getValue();
        }

        @UpdateRequestMethodMapper(LOOKUP_WORD)
        public UpdateResponse lookupByWord(
                UpdateRequest updateRequest,
                @UpdateRequestBodyParameterMapper UpdateChat chat
        ){
            StringBuilder sb = new StringBuilder();
            sb.append(updateRequest.command()).append(" : ");
            sb.append(updateRequest.value()).append(" : ");
            sb.append(chat.getMessageId());

            return new UpdateResponse(SendType.MESSAGE, sb.toString());
        }

        @UpdateRequestMethodMapper(HELP)
        public UpdateResponse help(UpdateRequest request){
            return new UpdateResponse(SendType.MESSAGE, "help! "+request.getValue());
        }
    }

}


