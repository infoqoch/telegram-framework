package infoqoch.dictionarybot.controller.resolver.returns;

import infoqoch.dictionarybot.controller.resolver.DictionariesUpdateRequestReturn;
import infoqoch.dictionarybot.controller.resolver.DictionaryUpdateRequestReturn;
import infoqoch.dictionarybot.mock.data.MockDictionary;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContentMarkdownPrinter;
import infoqoch.telegram.framework.update.resolver.returns.MSBUpdateRequestReturn;
import infoqoch.telegram.framework.update.resolver.returns.StringUpdateRequestReturn;
import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturn;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static infoqoch.dictionarybot.mock.data.MockDictionary.createSimpleDictionary;
import static infoqoch.dictionarybot.mock.data.MockDictionary.createSimpleDictionaryContent;
import static org.assertj.core.api.Assertions.assertThat;

// UpdateRequestMessageReturn의 리스트가 stream을 통해 적절한 데이터타입리졸버를 리턴함을 확인한다.
class UpdateRequestCommandAndValueReturnListTest {
    List<UpdateRequestReturn> returnResolvers;

    @BeforeEach
    void setUp(){
        returnResolvers = new ArrayList<>();
        returnResolvers.add(new DictionaryUpdateRequestReturn());
        returnResolvers.add(new MSBUpdateRequestReturn());
        returnResolvers.add(new StringUpdateRequestReturn());
        returnResolvers.add(new DictionariesUpdateRequestReturn());
    }

    @Test
    void dictionary(){
        // give
        final Dictionary target = MockDictionary.createSimpleDictionary(createSimpleDictionaryContent(), 123);

        // when
        final Optional<UpdateRequestReturn> resolver = returnResolvers.stream().filter(r -> r.support(target)).findAny();

        //then
        Assertions.assertThat(resolver).isPresent();
        assertThat(resolver.get()).isInstanceOf(DictionaryUpdateRequestReturn.class);
        assertThat(resolver.get().resolve(target).getMessage()).usingRecursiveComparison().isEqualTo(new DictionaryContentMarkdownPrinter(target).toMarkdown());
    }

    @Test
    void dictionaries(){
        // give
        final Dictionary d1 = createSimpleDictionary(createSimpleDictionaryContent(), 123);
        final Dictionary d2 = createSimpleDictionary(createSimpleDictionaryContent(), 123);
        List<Dictionary> target = new ArrayList<>();
        target.add(d1);
        target.add(d2);

        // when
        final Optional<UpdateRequestReturn> resolver = returnResolvers.stream().filter(r -> r.support(target)).findAny();

        //then
        Assertions.assertThat(resolver).isPresent();
        assertThat(resolver.get()).isInstanceOf(DictionariesUpdateRequestReturn.class);
        assertThat(resolver.get().resolve(target).getMessage().toString()).contains(new DictionaryContentMarkdownPrinter(d1).toMarkdown().toString());
        assertThat(resolver.get().resolve(target).getMessage().toString()).contains(new DictionaryContentMarkdownPrinter(d2).toMarkdown().toString());
    }
}