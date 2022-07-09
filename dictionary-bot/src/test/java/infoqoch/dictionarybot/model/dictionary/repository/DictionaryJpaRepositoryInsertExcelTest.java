package infoqoch.dictionarybot.model.dictionary.repository;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.service.DictionaryInsertBatchService;
import infoqoch.dictionarybot.model.user.ChatUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test_jpa")
@SpringBootTest
@Transactional
class DictionaryJpaRepositoryInsertExcelTest {

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

        ChatUser chatUser = new ChatUser(123l, "kim");
        em.persist(chatUser);

        // when
        dictionaryInsertBatchService.saveExcel(file, chatUser);
        em.flush();
        em.clear();

        // then
        final List<Dictionary> result = repository.findAll();
        assertThat(result).size().isEqualTo(47);
        assertThat(result.get(0).getChatUser().getChatId()).isEqualTo(123l);
    }
}