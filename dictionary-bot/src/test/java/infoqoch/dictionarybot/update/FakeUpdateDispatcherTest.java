package infoqoch.dictionarybot.update;

import infoqoch.dictionarybot.update.request.UpdateRequestCommand;
import infoqoch.dictionarybot.update.request.UpdateWrapper;
import infoqoch.dictionarybot.update.request.body.MockUpdateGenerate;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FakeUpdateDispatcherTest {

    private UpdateDispatcher updateDispatcher;

    @BeforeEach
    void setUp() {
        updateDispatcher = FakeUpdateDispatcherFactory.instance();
    }

    @Test
    void resolver_default_signature_test(){
        // given
        UpdateWrapper update = MockUpdateGenerate.jsonToUpdateWrapper(MockUpdateGenerate.documentJson("/help_hello!"));

        UpdateResponse response = updateDispatcher.process(update);

        // then
        assertThat(response.type()).isEqualTo(SendType.MESSAGE);
        assertThat(response.body()).isInstanceOf(String.class);
        assertThat((String) response.body()).isEqualTo("help! hello!");
    }

    @Test
    void resolver_custom_parameter(){
        // given
        UpdateWrapper update = MockUpdateGenerate.jsonToUpdateWrapper(MockUpdateGenerate.documentJson("/s_orange"));

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
        UpdateWrapper update = MockUpdateGenerate.jsonToUpdateWrapper(MockUpdateGenerate.documentJson("/w_apple"));

        UpdateResponse response = updateDispatcher.process(update);

        // then
        assertThat(response.type()).isEqualTo(SendType.MESSAGE);
        assertThat(response.body()).isInstanceOf(String.class);
        assertThat((String) response.body()).isEqualTo("LOOKUP_WORD : apple : 2149");
    }

    @Test
    void not_support_update_command(){
        UpdateWrapper update = MockUpdateGenerate.jsonToUpdateWrapper(MockUpdateGenerate.documentJson("/wefwe"));
        assertThat(update.command()).isEqualTo(UpdateRequestCommand.UNKNOWN);
        assertThat(update.value()).isEqualTo("wefwe");
    }
}


