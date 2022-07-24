package infoqoch.dictionarybot.model.dictionary.service;

import infoqoch.dictionarybot.mock.repository.MemoryDictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionarySource;
import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.repository.DictionarySourceRepository;
import infoqoch.dictionarybot.model.user.ChatUser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class DictionaryInsertBatchServiceTest {
    DictionaryRepository dictionaryRepository = new MemoryDictionaryRepository();
    DictionarySourceRepository dictionarySourceRepository = mock(DictionarySourceRepository.class);
    DictionaryInsertBatchService dictionaryInsertBatchService = new DictionaryInsertBatchService(dictionaryRepository, dictionarySourceRepository);

    @Test
    void save(){
        // gievn
        final ChatUser chatUser = new ChatUser(123l, "kim");
        File file = new File(getClass().getClassLoader().getResource("exceltest/sample.xlsx").getFile());
        DictionarySource source = new DictionarySource("wefijw", DictionarySource.Type.EXCEL, chatUser);
        given(dictionarySourceRepository.save(any())).willReturn(source);

        // when
        dictionaryInsertBatchService.saveExcel(file, source, chatUser);

        // then
        final List<Dictionary> find = dictionaryRepository.findAll();
        assertThat(find).size().isEqualTo(47);
    }
};