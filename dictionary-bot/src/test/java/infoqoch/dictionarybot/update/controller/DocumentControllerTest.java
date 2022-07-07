package infoqoch.dictionarybot.update.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.repository.MemoryDictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.service.DictionaryInsertBatchService;
import infoqoch.dictionarybot.update.controller.file.TelegramFileHandler;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.request.body.UpdateDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static infoqoch.dictionarybot.mock.data.MockUpdate.excelDocumentJson;
import static infoqoch.dictionarybot.mock.data.MockUpdate.jsonToUpdateWrapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DocumentControllerTest {
    DocumentController dictionaryController;
    DictionaryInsertBatchService dictionaryInsertBatchService;
    DictionaryRepository dictionaryRepository;
    TelegramFileHandler mockHandler;

    @BeforeEach
    void setUp(){
        dictionaryRepository = new MemoryDictionaryRepository();
        dictionaryInsertBatchService = new DictionaryInsertBatchService(dictionaryRepository);
        mockHandler = mock(TelegramFileHandler.class);
        dictionaryController = new DocumentController(dictionaryRepository, dictionaryInsertBatchService, mockHandler);
    }

    @Test
    void test() throws JsonProcessingException {
        // given
        final UpdateDocument document = mockDocumentWithExcel("/excel_push");
        File file = new File(getClass().getClassLoader().getResource("exceltest/sample.xlsx").getFile());
        when(mockHandler.extractExcelFile(any())).thenReturn(file);

        // when
        dictionaryController.excelPush(document);

        // then
        assertThat(dictionaryRepository.findAll().size()).isEqualTo(47);
    }

    private UpdateDocument mockDocumentWithExcel(String caption) {
        final String mockJson = excelDocumentJson(caption);
        final UpdateRequest updateRequest = jsonToUpdateWrapper(mockJson);
        final UpdateDocument document = updateRequest.toDocument();
        return document;
    }
}