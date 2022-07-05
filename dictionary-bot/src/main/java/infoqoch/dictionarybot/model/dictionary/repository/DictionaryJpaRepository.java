package infoqoch.dictionarybot.model.dictionary.repository;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Profile("!test")
@Repository
public interface DictionaryJpaRepository extends DictionaryRepository, JpaRepository<Dictionary, Long> {
}

