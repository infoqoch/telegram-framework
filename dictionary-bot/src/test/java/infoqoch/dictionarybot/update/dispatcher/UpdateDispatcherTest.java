package infoqoch.dictionarybot.update.dispatcher;

import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.mock.update.FakeUpdateDispatcherFactory;
import infoqoch.dictionarybot.update.request.UpdateRequestCommand;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.mock.data.MockUpdate;
import infoqoch.dictionarybot.send.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// UpdateDispatcher의 생성에 대한 테스트를 진행한다.
// 특히 ParamResolver와 ReturnResolver를 테스트한다.
// FakeController 의 설정에 따르므로, 해당 객체와 비교하며 테스트를 진행한다. (중요!)
// 아래 테스트의 네이밍 규칙은 {param|return}{support type} 이다.
public class UpdateDispatcherTest {
    private UpdateDispatcher updateDispatcher;

    @BeforeEach
    void setUp() {
        updateDispatcher = FakeUpdateDispatcherFactory.defaultInstance();
    }

    @Test
    void param_UpdateRequestBodyParameterMapper(){
        // given
        UpdateRequest update = MockUpdate.jsonToUpdateRequest(MockUpdate.documentJson("/help_hello!"));

        UpdateResponse response = updateDispatcher.process(update);

        // then
        assertThat(response.getSendType()).isEqualTo(SendType.MESSAGE);
        assertThat(response.getMessage()).isInstanceOf(MarkdownStringBuilder.class);
        assertThat(response.getMessage()).usingRecursiveComparison().isEqualTo(new MarkdownStringBuilder("help! hello!"));
    }

    @Test
    void param_UpdateRequest_return_string(){
        // given
        UpdateRequest update = MockUpdate.jsonToUpdateRequest(MockUpdate.documentJson("/s_orange"));

        // when
        UpdateResponse response = updateDispatcher.process(update);

        // then
        assertThat(response.getSendType()).isEqualTo(SendType.MESSAGE);
        assertThat(response.getMessage()).isInstanceOf(MarkdownStringBuilder.class);
        assertThat(response.getMessage()).usingRecursiveComparison().isEqualTo(new MarkdownStringBuilder("LOOKUP_SENTENCE : orange"));
    }

    @Test
    void return_UpdateResponse(){
        // given
        UpdateRequest update = MockUpdate.jsonToUpdateRequest(MockUpdate.documentJson("/w_apple"));

        UpdateResponse response = updateDispatcher.process(update);

        // then
        assertThat(response.getSendType()).isEqualTo(SendType.MESSAGE);
        assertThat(response.getMessage()).isInstanceOf(MarkdownStringBuilder.class);
        assertThat(response.getMessage()).usingRecursiveComparison().isEqualTo(new MarkdownStringBuilder("LOOKUP_WORD : apple : 2149"));
    }

    @Test
    void return_UpdateResponse_null_body(){
        // given
        UpdateRequest update = MockUpdate.jsonToUpdateRequest(MockUpdate.documentJson("/d_hi"));

        UpdateResponse response = updateDispatcher.process(update);

        // then
        assertThat(response.getSendType()).isEqualTo(SendType.MESSAGE);
        assertThat(response.getMessage().toString()).contains("*apple*");
    }

    @Test
    void unknown_command(){
        //when
        UpdateRequest update = MockUpdate.jsonToUpdateRequest(MockUpdate.documentJson("/wefwe"));

        UpdateResponse response = updateDispatcher.process(update);

        //then
        assertThat(update.command()).isEqualTo(UpdateRequestCommand.UNKNOWN);
        assertThat(update.value()).isEqualTo("wefwe"); // command를 알 수 없으면 value를 요청한 값으로 한다.
        assertThat(response.getMessage()).usingRecursiveComparison().isEqualTo(new MarkdownStringBuilder("unknown??"));
    }

    @Test
    void multiple_commands_first(){
        //given
        UpdateRequest update = MockUpdate.jsonToUpdateRequest(MockUpdate.documentJson("/status"));

        // when
        UpdateResponse response = updateDispatcher.process(update);

        //then
        assertThat(update.command()).isEqualTo(UpdateRequestCommand.MY_STATUS);
        assertThat(update.value()).isEmpty();
        assertThat(response.getMessage()).usingRecursiveComparison().isEqualTo(new MarkdownStringBuilder("multipleCommands called"));
    }

    @Test
    void multiple_commands_second(){
        //given
        UpdateRequest update = MockUpdate.jsonToUpdateRequest(MockUpdate.documentJson("/f_multiple_command"));

        // when
        UpdateResponse response = updateDispatcher.process(update);

        //then
        assertThat(update.command()).isEqualTo(UpdateRequestCommand.LOOKUP_FULL_SEARCH);
        assertThat(update.value()).isEqualTo("multiple command");
        assertThat(response.getMessage()).usingRecursiveComparison().isEqualTo(new MarkdownStringBuilder("multipleCommands called"));
    }
}

