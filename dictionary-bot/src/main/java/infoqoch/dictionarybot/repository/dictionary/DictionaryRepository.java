package infoqoch.dictionarybot.repository.dictionary;

import java.util.Optional;

public interface DictionaryRepository {
    Long save(Dictionary dictionary);

    Optional<Dictionary> findByNo(Long dictionaryNo);
}
