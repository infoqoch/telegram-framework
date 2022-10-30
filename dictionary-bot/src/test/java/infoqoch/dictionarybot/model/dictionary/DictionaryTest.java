package infoqoch.dictionarybot.model.dictionary;

import infoqoch.dictionarybot.mock.data.MockDictionary;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DictionaryTest {
    @DisplayName("Dictionary의 markdown이 잘 추출되는지 확인한다")
    @Test
    void toMarkDown() {
        final Dictionary simpleDictionary = MockDictionary.createSimpleDictionary(MockDictionary.createSimpleDictionaryContent(), 123l);
        MarkdownStringBuilder msb =  new DictionaryContentMarkdownPrinter(simpleDictionary).toMarkdown();
        assertThat(msb.toString()).contains("*apple*\\(애포얼\\)", "_사과_, Iphone 7 is the latest model");
    }
}