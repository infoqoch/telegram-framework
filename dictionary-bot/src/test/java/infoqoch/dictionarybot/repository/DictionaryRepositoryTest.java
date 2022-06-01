package infoqoch.dictionarybot.repository;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class DictionaryRepositoryTest {
    DictionaryRepository repository;

    @Test
    void save(){
        repository = new MemoryDictionaryRepository();

        DictionaryContent content = DictionaryContent.builder()
                .word("apple")
                .pronunciation("애포얼")
                .partOfSpeech("noun")
                .source("아낌없이 주는 나무")
                .definition("사과")
                .sentence("Iphone 7 is the latest model")
                .build();

        final Dictionary dictionary = Dictionary.builder().content(content).build();
        final Long dictionaryNo = repository.save(dictionary);

        assertThat(dictionaryNo).isGreaterThan(0);
    }

    @Test
    void find_by_no(){
        // given
        repository = new MemoryDictionaryRepository();

        DictionaryContent content = DictionaryContent.builder()
                .word("apple")
                .pronunciation("애포얼")
                .partOfSpeech("noun")
                .source("아낌없이 주는 나무")
                .definition("사과")
                .sentence("Iphone 7 is the latest model")
                .build();

        final Dictionary dictionary = Dictionary.builder().content(content).build();
        final Long dictionaryNo = repository.save(dictionary);

        // when
        Optional<Dictionary> result = repository.findByNo(dictionaryNo);
        assertThat(result).isPresent();
        assertThat(result.get().getNo()).isEqualTo(dictionaryNo);
        assertThat(result.get().getContent()).usingRecursiveComparison().isEqualTo(content);
    }
}
