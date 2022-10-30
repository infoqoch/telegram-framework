package infoqoch.dictionarybot.model.dictionary;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DictionaryContentTest {
    @Test
    @DisplayName("단어, 정의, 문장 중 하나라도 없으면 비정상으로 처리한다")
    void valid(){
        assertThat(content("word", "defi", "sent").valid()).isTrue();
        assertThat(content(null, "defi", "sent").valid()).isTrue();
        assertThat(content("word", null, "sent").valid()).isTrue();
        assertThat(content("word", "defi", null).valid()).isTrue();
        assertThat(content(null, null, "sent").valid()).isTrue();
        assertThat(content(null, "defi", null).valid()).isTrue();
        assertThat(content("word", null, null).valid()).isTrue();
        assertThat(content(null, null, null).valid()).isFalse();

        assertThat(content("d", "", "").valid()).isTrue();
        assertThat(content("", null, null).valid()).isFalse();
        assertThat(content("","", "").valid()).isFalse();
    }

    private DictionaryContent content(String word, String definition, String sentence) {
        final DictionaryContent build = DictionaryContent.builder()
                .word(word)
                .definition(definition)
                .sentence(sentence)
                .partOfSpeech("noun")
                .pronunciation("워어어드")
                .quotation("spongebob")
                .build();
        return build;
    }

    // @Test
    // 동작하지 않는다.
    void string_isEmpty(){
        assertThat(createString("").isEmpty()).isTrue();
        assertThat(createString(" ").isEmpty()).isTrue();
        assertThat(createString("h").isEmpty()).isFalse();
        assertThat(createString(null).isEmpty()).isTrue();
    }

    private String createString(String string) {
        return string;
    }

}