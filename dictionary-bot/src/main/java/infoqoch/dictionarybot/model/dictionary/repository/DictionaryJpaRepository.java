package infoqoch.dictionarybot.model.dictionary.repository;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictionaryJpaRepository extends DictionaryRepository, JpaRepository<Dictionary, Long> {
    List<Dictionary> findByContentWordStartsWith(String value);

    List<Dictionary> findByContentWordEndsWith(String value);

    List<Dictionary> findByContentWordContains(String value);

    List<Dictionary> findByContentDefinitionStartsWith(String value);

    List<Dictionary> findByContentDefinitionEndsWith(String value);

    List<Dictionary> findByContentDefinitionContains(String value);

    List<Dictionary> findByContentSentenceStartsWith(String value);

    List<Dictionary> findByContentSentenceEndsWith(String value);

    List<Dictionary> findByContentSentenceContains(String value);
}

