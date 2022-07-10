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

class DictionaryInsertBatchServiceExcelTest {

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
        // TODO
        // MemoryRepository에는 아래의 기능이 구현되지 않음.
        // 이 부분을 구현할지, 아니면 다른 방식으로 할지 고민이 됨. 계속 구현해야 할 부분이 늘어날 텐데 어떻게 대응할지 고민.
        // 다만 saveExcel 를 테스트한다는 의미에서 mock을 삽입하고 어떤 역할도 하지 않는 것은 큰 문제는 아님. 오히려 spy로 DictionarySourceRepository#save를 touch 하는 것만 확인해도 충분한 테스트이므로.
//        assertThat(find.get(0).getSource().getFileId()).isEqualTo("wefijw");
//        assertThat(find.get(0).getChatUser().getChatId()).isEqualTo(123l);
    }
};