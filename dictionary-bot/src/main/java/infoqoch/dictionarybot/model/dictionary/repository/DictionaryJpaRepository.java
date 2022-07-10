package infoqoch.dictionarybot.model.dictionary.repository;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictionaryJpaRepository extends JpaRepository<Dictionary, Long>, DictionaryRepository {
    List<Dictionary> findByNoIn(List<Long> ids);
}

