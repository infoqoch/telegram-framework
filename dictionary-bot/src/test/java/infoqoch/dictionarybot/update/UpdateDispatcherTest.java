package infoqoch.dictionarybot.update;

import infoqoch.dictionarybot.update.testcontroller.TestHandler;
import infoqoch.dictionarybot.update.request.body.MockUpdateJsonGenerate;
import infoqoch.dictionarybot.update.request.UpdateWrapper;
import infoqoch.dictionarybot.update.resolver.bean.MapBeanContext;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateDispatcherTest {

    private UpdateDispatcher updateDispatcher;

    @BeforeEach
    void setUp() {
        Map<Class<?>, Object> context = new HashMap<>();
        final Object bean = new TestHandler();
        context.put(bean.getClass(), bean);

        MapBeanContext beanExtract = new MapBeanContext();
        beanExtract.setContext(context);

        final String path = this.getClass().getPackage().getName() + ".testcontroller";
        updateDispatcher = new UpdateDispatcher(path, beanExtract);
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

}


