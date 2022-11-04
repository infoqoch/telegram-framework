package infoqoch.telegram.framework.update.controller.resolver.returns;

import infoqoch.dictionarybot.controller.resolver.DictionaryUpdateRequestReturn;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturn;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import org.junit.jupiter.api.Test;

import static infoqoch.dictionarybot.mock.data.MockDictionary.createSimpleDictionary;
import static infoqoch.dictionarybot.mock.data.MockDictionary.createSimpleDictionaryContent;
import static org.assertj.core.api.Assertions.assertThat;

// TODO
// UpdateRequestMessageReturn으로 구현한 타입의 정상 동작여부를 확인한다.
// 부분적으로 테스트 코드를 작성하였다. 차후 전체 타입에 대한 테스트를 진행한다.
class UpdateRequestCommandAndValueReturnTest {
    @Test
    void dictionary(){
        final Dictionary simpleDictionary = createSimpleDictionary(createSimpleDictionaryContent(), 123l);
        assertSingleDictionary(simpleDictionary, "*apple*\\(애포얼\\)", "_사과_, Iphone 7 is the latest model");
    }

    private void assertSingleDictionary(Dictionary target, String...expected) {
        // given
        UpdateRequestReturn resolver = new DictionaryUpdateRequestReturn();

        // support
        boolean isSupport = resolver.support(target);
        assertThat(isSupport).isTrue();

        // resolve
        UpdateResponse result =  resolver.resolve(target);
        assertThat(result.getMessage().toString()).contains(expected);
    }
}