package infoqoch.mock.repository;

import infoqoch.dictionarybot.model.dictionary.repository.LookupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

/*
*
* @DataJpaTest는 querydsl을 빈으로 등록하지 못한다.
* @TestConfiguration을 통하여 빈을 등록해야 한다.
*
*/
@TestConfiguration
public class QuerydslConfig {
    @Autowired
    EntityManager em;

    @Bean
    public LookupRepository lookupRepository() {
        return new LookupRepository(em);
    }
}
