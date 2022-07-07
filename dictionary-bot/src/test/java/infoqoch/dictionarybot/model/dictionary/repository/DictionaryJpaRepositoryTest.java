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
@ActiveProfiles("dev") // test의 경우 Memory 등 DB를 직접 사용하지 않음.
@Transactional
class DictionaryJpaRepositoryTest {

    @Autowired
    DictionaryInsertBatchService dictionaryInsertBatchService;

    @Autowired
    DictionaryRepository repository;

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