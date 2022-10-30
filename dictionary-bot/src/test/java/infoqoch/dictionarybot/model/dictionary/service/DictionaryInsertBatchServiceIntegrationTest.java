package infoqoch.dictionarybot.model.dictionary.service;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionarySource;
import infoqoch.dictionarybot.model.dictionary.repository.DictionaryJpaRepository;
import infoqoch.dictionarybot.model.user.ChatUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DictionaryInsertBatchServiceIntegrationTest {

    @Autowired
    DictionaryInsertBatchService dictionaryInsertBatchService;

    @Autowired
    DictionaryJpaRepository repository;

    @Autowired
    EntityManager em;

    @Test
    void insert_excel_dictionaries_with_jpa(){
        // given
        File file = new File(getClass().getClassLoader().getResource("exceltest/dictionary_test.xlsx").getFile());

        ChatUser chatUser = new ChatUser(ThreadLocalRandom.current().nextLong(), "kim");
        em.persist(chatUser);

        final DictionarySource source = new DictionarySource("aberiuwefwef", DictionarySource.Type.EXCEL, chatUser);

        // when
        final List<Dictionary> saved = dictionaryInsertBatchService.saveExcel(file, source, chatUser);
        em.flush();
        em.clear();

        // then
        final List<Dictionary> result = repository.findByNoIn(saved.stream().map(d -> d.getNo()).collect(Collectors.toList()));
        assertThat(result).size().isEqualTo(47);
        assertThat(result.get(0).getChatUser().getChatId()).isEqualTo(chatUser.getChatId());
        assertThat(result.get(0).getSource().getFileId()).isEqualTo("aberiuwefwef");
    }

    @Test
    void delete_child_cascade(){
        // given
        File file = new File(getClass().getClassLoader().getResource("exceltest/dictionary_test.xlsx").getFile());
        ChatUser chatUser = new ChatUser(ThreadLocalRandom.current().nextLong(), "kim");
        em.persist(chatUser);
        
        // source 첫 번째
        final DictionarySource source1 = new DictionarySource("aberiuwefwef", DictionarySource.Type.EXCEL, chatUser);
        dictionaryInsertBatchService.saveExcel(file, source1, chatUser);
        
        // source 두 번째
        final DictionarySource source2 = new DictionarySource("aberiuwefwef", DictionarySource.Type.EXCEL, chatUser);
        dictionaryInsertBatchService.saveExcel(file, source2, chatUser);
        em.flush();
        em.clear();

        // check if exist
        final List<Dictionary> findDictionary = repository.findByChatUser(chatUser);
        assert findDictionary.size() == (47*2);
        em.flush();
        em.clear();

        // when
        // source 첫 번째에 대해서만 삭제한다.
        dictionaryInsertBatchService.deleteSourcesExclude(source2, chatUser);
        em.flush();
        em.clear();

        final List<Dictionary> targetDictionary = repository.findByChatUser(chatUser);
        assertThat(targetDictionary).size().isEqualTo(47);
        assertThat(targetDictionary.get(0).getSource().getNo()).isEqualTo(source2.getNo());
    }
}