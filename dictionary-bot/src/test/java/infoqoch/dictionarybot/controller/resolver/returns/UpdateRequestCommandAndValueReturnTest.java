package infoqoch.dictionarybot.controller.resolver.returns;

import infoqoch.dictionarybot.controller.resolver.DictionariesUpdateRequestReturn;
import infoqoch.dictionarybot.controller.resolver.DictionaryUpdateRequestReturn;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContentMarkdownPrinter;
import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturn;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static infoqoch.mock.data.MockDictionary.createSimpleDictionary;
import static infoqoch.mock.data.MockDictionary.createSimpleDictionaryContent;
import static org.assertj.core.api.Assertions.assertThat;

class UpdateRequestCommandAndValueReturnTest {
    @Test
    void dictionary(){
        // given
        final Dictionary simpleDictionary = createSimpleDictionary(createSimpleDictionaryContent(), 123l);

        // support
        UpdateRequestReturn resolver = new DictionaryUpdateRequestReturn();
        assertThat(resolver.support(simpleDictionary)).isTrue();

        // resolve
        UpdateResponse result =  resolver.resolve(simpleDictionary);
        assertThat(result.getMessage().toString()).contains(new String[]{"*apple*\\(애포얼\\)", "_사과_, Iphone 7 is the latest model"});
    }

    @Test
    void dictionaries(){
        // give
        final Dictionary d1 = createSimpleDictionary(createSimpleDictionaryContent(), 123);
        final Dictionary d2 = createSimpleDictionary(createSimpleDictionaryContent(), 123);
        List<Dictionary> target = new ArrayList<>();
        target.add(d1);
        target.add(d2);

        // support
        final UpdateRequestReturn resolver = new DictionariesUpdateRequestReturn();
        assertThat(resolver.support(target)).isTrue();

        // resolve
        final UpdateResponse result = resolver.resolve(target);
        assertThat(result.getMessage().toString()).isEqualTo(toMarkdown(target).toString());
    }

    private MarkdownStringBuilder toMarkdown(List<Dictionary> dictionaries) {
        MarkdownStringBuilder msb = new MarkdownStringBuilder();
        for (Dictionary dictionary : dictionaries) {
            msb.append(new DictionaryContentMarkdownPrinter(dictionary).toMarkdown()).lineSeparator();
        }
        return msb;
    }
}