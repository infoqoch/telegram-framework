package infoqoch.dictionarybot.model.dictionary;

import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DictionaryTest {
    @Test
    void printOne(){
        final Dictionary simpleDictionary = createSimpleDictionary(createSimpleDictionaryContent(), 123l);
        MarkdownStringBuilder msb = simpleDictionary.toMarkdown();
        assertThat(msb.toString()).contains("*apple*\\(애포얼\\)", "_사과_, Iphone 7 is the latest model");
    }

    private Dictionary createSimpleDictionary(DictionaryContent content, long no) {
        return Dictionary.builder().no(no).content(content).build();
    }

    private DictionaryContent createSimpleDictionaryContent() {
        DictionaryContent content = DictionaryContent.builder()
                .word("apple")
                .pronunciation("애포얼")
                .partOfSpeech("noun")
                .source("아낌없이 주는 나무")
                .definition("사과")
                .sentence("Iphone 7 is the latest model")
                .build();
        return content;
    }
}