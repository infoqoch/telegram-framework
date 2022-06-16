package infoqoch.dictionarybot.model.dictionary.service;

import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.repository.MemoryDictionaryRepository;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class DictionaryServiceTest {
    DictionaryRepository dictionaryRepository = new MemoryDictionaryRepository();
    DictionaryService dictionaryService = new DictionaryService(dictionaryRepository);
    @Test
    void test(){
        File file = new File(getClass().getClassLoader().getResource("exceltest/sample.xlsx").getFile());
        dictionaryService.saveExcel(file);

        assertThat(dictionaryRepository.findAll()).size().isEqualTo(47);
    }
};