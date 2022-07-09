package infoqoch.dictionarybot.model.dictionary.service;

import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.mock.repository.MemoryDictionaryRepository;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class DictionaryInsertBatchServiceExcelTest {
    DictionaryRepository dictionaryRepository = new MemoryDictionaryRepository();
    DictionaryInsertBatchService dictionaryInsertBatchService = new DictionaryInsertBatchService(dictionaryRepository);
    @Test
    void test(){
        File file = new File(getClass().getClassLoader().getResource("exceltest/sample.xlsx").getFile());
        dictionaryInsertBatchService.saveExcel(file);
        assertThat(dictionaryRepository.findAll()).size().isEqualTo(47);
    }
};