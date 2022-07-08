package infoqoch.dictionarybot.model.dictionary.repository;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.service.DictionaryInsertBatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.List;

@SpringBootTest
@ActiveProfiles("dev") // test의 경우 jpa를 사용하지 않는 메모리 DB만 바라봄. dev 이상부터 jpa를 사용함.
@Transactional
class DictionaryJpaRepositoryTest {

    @Autowired
    DictionaryInsertBatchService dictionaryInsertBatchService;

    @Autowired
    DictionaryJpaRepository repository;

    @Autowired
    EntityManager em;

    @Test
    void insert_excel_dictionaries_with_jpa(){
        // given
        File file = new File(getClass().getClassLoader().getResource("exceltest/sample.xlsx").getFile());

        // when
        dictionaryInsertBatchService.saveExcel(file);
        em.flush();
        em.clear();

        // then
        final List<Dictionary> all = repository.findAll();
        System.out.println(all.size());
    }
}