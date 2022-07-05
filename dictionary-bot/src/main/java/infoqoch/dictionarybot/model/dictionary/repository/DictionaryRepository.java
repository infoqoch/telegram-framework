package infoqoch.dictionarybot.model.dictionary.repository;

import infoqoch.dictionarybot.model.dictionary.Dictionary;

import java.util.List;
import java.util.Optional;

public interface DictionaryRepository {
    Dictionary save(Dictionary dictionary);

    Optional<Dictionary> findByNo(Long dictionaryNo);

    List<Dictionary> findByContentWord(String word);

    List<Dictionary> findByContentSentence(String sentence);

    List<Dictionary> findByContentDefinition(String definition);

    List<Dictionary> findAll();
}
