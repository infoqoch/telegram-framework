package infoqoch.dictionarybot.update.controller;

import infoqoch.dictionarybot.mock.repository.MemoryDictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionarySource;
import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.repository.DictionarySourceRepository;
import infoqoch.dictionarybot.model.dictionary.service.DictionaryInsertBatchService;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.update.controller.file.TelegramFileHandler;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.request.body.UpdateDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static infoqoch.dictionarybot.mock.data.MockUpdate.excelDocumentJson;
import static infoqoch.dictionarybot.mock.data.MockUpdate.jsonToUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DocumentControllerTest {
    DocumentController dictionaryController;
    DictionaryInsertBatchService dictionaryInsertBatchService;
    DictionaryRepository dictionaryRepository;
    DictionarySourceRepository dictionarySourceRepository;
    TelegramFileHandler mockHandler;

    @BeforeEach
    void setUp(){
        dictionaryRepository = new MemoryDictionaryRepository();
        dictionarySourceRepository = mock(DictionarySourceRepository.class);
        dictionaryInsertBatchService = new DictionaryInsertBatchService(dictionaryRepository, dictionarySourceRepository);
        mockHandler = mock(TelegramFileHandler.class);
        dictionaryController = new DocumentController(dictionaryRepository, dictionaryInsertBatchService, mockHandler);
    }

    @Test
    void excel_push() {
        // given
        when(mockHandler.extractExcelFile(any())).thenReturn(new File(getClass().getClassLoader().getResource("exceltest/sample.xlsx").getFile()));

        final ChatUser chatUser = new ChatUser(123l, "kim");

        DictionarySource source = new DictionarySource("wefijw", DictionarySource.Type.EXCEL, chatUser);
        given(dictionarySourceRepository.save(any())).willReturn(source);

        // when
        dictionaryController.excelPush(mockDocumentWithExcel("/excel_push"), chatUser);

        // then
        final List<Dictionary> result = dictionaryRepository.findAll();
        assertThat(result.size()).isEqualTo(47);
    }

    private UpdateDocument mockDocumentWithExcel(String caption) {
        final String mockJson = excelDocumentJson(caption);
        final UpdateRequest updateRequest = jsonToUpdateRequest(mockJson);
        final UpdateDocument document = updateRequest.toDocument();
        return document;
    }
}