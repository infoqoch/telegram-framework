package infoqoch.dictionarybot.model.dictionary.repository;

import infoqoch.dictionarybot.model.dictionary.Dictionary;

import java.util.List;
import java.util.Optional;

public interface DictionaryRepository {
    Long save(Dictionary dictionary);

    void save(List<Dictionary> dictionaries);

    Optional<Dictionary> findByNo(Long dictionaryNo);

    List<Dictionary> findByWord(String target);

    List<Dictionary> findBySentence(String target);

    List<Dictionary> findByDefinition(String target);

    List<Dictionary> findAll();
}
