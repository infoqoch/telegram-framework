package infoqoch.dictionarybot.repository.dictionary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class MemoryDictionaryRepositoryTest {
    DictionaryRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MemoryDictionaryRepository();
    }

    @Test
    void save(){
        final Long dictionaryNo = saveDictionaryInRepo(createSimpleDictionaryContent("apple"));
        assertThat(dictionaryNo).isGreaterThan(0);
    }

    @Test
    void find_by_no(){
        // given
        final DictionaryContent content = createSimpleDictionaryContent("first");
        final Long dictionaryNo = saveDictionaryInRepo(content);

        // when
        Optional<Dictionary> result = repository.findByNo(dictionaryNo);

        // then
        assertThat(result).isPresent();
        assertThat(dictionaryNo).isEqualTo(1l);
        assertThat(result.get().getNo()).isEqualTo(dictionaryNo);
        assertThat(result.get().getContent()).usingRecursiveComparison().isEqualTo(content);
    }

    @Test
    void find_by_no_next_key(){
        final Long firstNo = saveDictionaryInRepo(createSimpleDictionaryContent("first"));
        assert firstNo == 1l;

        // given
        final DictionaryContent content = createSimpleDictionaryContent("second");
        final Long dictionaryNo = saveDictionaryInRepo(content);

        // when
        Optional<Dictionary> result = repository.findByNo(dictionaryNo);

        // then
        assertThat(result).isPresent();
        assertThat(dictionaryNo).isEqualTo(2l);
        assertThat(result.get().getNo()).isEqualTo(dictionaryNo);
        assertThat(result.get().getContent()).usingRecursiveComparison().isEqualTo(content);
    }

    @Test
    void find_by_word(){
        // given
        saveDictionaryInRepo(createSimpleDictionaryContent("kimchi")); // 허수
        DictionaryContent content = createSimpleDictionaryContent("apple");
        final Long dictionaryNo = saveDictionaryInRepo(content);
        saveDictionaryInRepo(createSimpleDictionaryContent("radio")); // 허수

        // when
        Optional<Dictionary> result = repository.findByWord("apple");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getNo()).isEqualTo(dictionaryNo);
        assertThat(result.get().getContent()).usingRecursiveComparison().isEqualTo(content);
    }


    private Long saveDictionaryInRepo(DictionaryContent dictionaryContent) {
        final Dictionary dictionary = Dictionary.builder().content(dictionaryContent).build();
        return repository.save(dictionary);
    }

    private DictionaryContent createSimpleDictionaryContent(String word) {
        DictionaryContent content = DictionaryContent.builder()
                .word(word)
                .pronunciation("애포얼")
                .partOfSpeech("noun")
                .source("아낌없이 주는 나무")
                .definition("사과")
                .sentence("Iphone 7 is the latest model")
                .build();
        return content;
    }
}
