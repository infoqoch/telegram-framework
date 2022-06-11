package infoqoch.dictionarybot.model.dictionary;

import java.util.List;
import java.util.Optional;

public interface DictionaryRepository {
    Long save(Dictionary dictionary);

    Optional<Dictionary> findByNo(Long dictionaryNo);

    List<Dictionary> findByWord(String target);
}
