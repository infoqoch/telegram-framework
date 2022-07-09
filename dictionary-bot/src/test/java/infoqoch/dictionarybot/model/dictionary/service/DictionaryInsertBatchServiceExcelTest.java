package infoqoch.dictionarybot.model.dictionary.service;

import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.mock.repository.MemoryDictionaryRepository;
import infoqoch.dictionarybot.model.user.ChatUser;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class DictionaryInsertBatchServiceExcelTest {
    DictionaryRepository dictionaryRepository = new MemoryDictionaryRepository();
    DictionaryInsertBatchService dictionaryInsertBatchService = new DictionaryInsertBatchService(dictionaryRepository);
    @Test
    void test(){
        final ChatUser chatUser = new ChatUser(123l, "kim");
        File file = new File(getClass().getClassLoader().getResource("exceltest/sample.xlsx").getFile());
        dictionaryInsertBatchService.saveExcel(file, chatUser);
        assertThat(dictionaryRepository.findAll()).size().isEqualTo(47);
    }
};