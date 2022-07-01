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

// FakeUpdateDispatcherFactory 에 등록된 가짜 빈에 한정함!
// param resolver, return resolver 를 특히 검사함.
public class UpdateDispatcherTest {

    private UpdateDispatcher updateDispatcher;

    @BeforeEach
    void setUp() {
        updateDispatcher = FakeUpdateDispatcherFactory.defaultInstance();
    }

    @Test
    void param_UpdateRequestBodyParameterMapper(){
        // given
        UpdateRequest update = MockUpdate.jsonToUpdateWrapper(MockUpdate.documentJson("/help_hello!"));

        UpdateResponse response = updateDispatcher.process(update);

        // then
        assertThat(response.type()).isEqualTo(SendType.MESSAGE);
        assertThat(response.message()).isInstanceOf(MarkdownStringBuilder.class);
        assertThat(response.message()).usingRecursiveComparison().isEqualTo(new MarkdownStringBuilder("help! hello!"));
    }

    @Test
    void param_UpdateRequest_return_string(){
        // given
        UpdateRequest update = MockUpdate.jsonToUpdateWrapper(MockUpdate.documentJson("/s_orange"));

        // when
        UpdateResponse response = updateDispatcher.process(update);

        // then
        assertThat(response.type()).isEqualTo(SendType.MESSAGE);
        assertThat(response.message()).isInstanceOf(MarkdownStringBuilder.class);
        assertThat(response.message()).usingRecursiveComparison().isEqualTo(new MarkdownStringBuilder("LOOKUP_SENTENCE : orange"));
    }

    @Test
    void return_UpdateResponse(){
        // given
        UpdateRequest update = MockUpdate.jsonToUpdateWrapper(MockUpdate.documentJson("/w_apple"));

        UpdateResponse response = updateDispatcher.process(update);

        // then
        assertThat(response.type()).isEqualTo(SendType.MESSAGE);
        assertThat(response.message()).isInstanceOf(MarkdownStringBuilder.class);
        assertThat(response.message()).usingRecursiveComparison().isEqualTo(new MarkdownStringBuilder("LOOKUP_WORD : apple : 2149"));
    }

    @Test
    void return_UpdateResponse_null_body(){
        // given
        UpdateRequest update = MockUpdate.jsonToUpdateWrapper(MockUpdate.documentJson("/d_hi"));

        UpdateResponse response = updateDispatcher.process(update);

        // then
        assertThat(response.type()).isEqualTo(SendType.MESSAGE);
        assertThat(response.message().text()).contains("*apple*");
    }

    @Test
    void unknown_command(){
        //when
        UpdateRequest update = MockUpdate.jsonToUpdateWrapper(MockUpdate.documentJson("/wefwe"));

        UpdateResponse response = updateDispatcher.process(update);

        //then
        assertThat(update.command()).isEqualTo(UpdateRequestCommand.UNKNOWN);
        assertThat(update.value()).isEqualTo("wefwe"); // command를 알 수 없으면 value를 요청한 값으로 한다.
        assertThat(response.message()).usingRecursiveComparison().isEqualTo(new MarkdownStringBuilder("unknown??"));
    }
}


