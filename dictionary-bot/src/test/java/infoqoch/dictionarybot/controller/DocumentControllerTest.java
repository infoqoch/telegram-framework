package infoqoch.dictionarybot.controller;

import infoqoch.mock.repository.MemoryDictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionarySource;
import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.repository.DictionarySourceRepository;
import infoqoch.dictionarybot.model.dictionary.service.DictionaryInsertBatchService;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.system.properties.DictionaryProperties;
import infoqoch.telegram.framework.update.util.TelegramProperties;
import infoqoch.telegram.framework.update.file.TelegramFileHandler;
import infoqoch.telegram.framework.update.exception.TelegramClientException;
import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.request.body.UpdateDocument;
import infoqoch.telegrambot.bot.entity.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static infoqoch.mock.data.MockUpdate.excelDocumentJson;
import static infoqoch.mock.data.MockUpdate.jsonToUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DocumentControllerTest {
    DocumentController dictionaryController;
    DictionaryInsertBatchService dictionaryInsertBatchService;
    DictionaryRepository memoryDictionaryRepository;
    DictionarySourceRepository mockDictionarySourceRepository;
    TelegramFileHandler mockFileHandler;
    TelegramProperties telegramProperties;
    DictionaryProperties dictionaryProperties;

    @BeforeEach
    void setUp(){
        dictionaryProperties = new DictionaryProperties(null, null);
        telegramProperties = TelegramProperties.generate();
        memoryDictionaryRepository = new MemoryDictionaryRepository();
        mockDictionarySourceRepository = mock(DictionarySourceRepository.class);
        dictionaryInsertBatchService = new DictionaryInsertBatchService(memoryDictionaryRepository, mockDictionarySourceRepository);
        mockFileHandler = mock(TelegramFileHandler.class);
        dictionaryController = new DocumentController(dictionaryInsertBatchService, mockFileHandler);
    }

    @Test
    void empty_document() {
        // given
        final UpdateDocument empty = UpdateDocument.builder().build();

        // then
        assertThatThrownBy(()->{
            dictionaryController.excelPush(empty, null);
        }).isInstanceOf(TelegramClientException.class).message().contains("document가 누락되었습니다.");
    }

    @Test
    @DisplayName("telegram 에서 document로 보낼 때, 그것의 데이터 타입(mimetype)을 전달한다. 이를 기반으로 정상여부를 판단한다. mime이 excel이나 실제로 excel이 아닌 경우는 상정하지 않는다.")
    void not_excel_mime_type() {
        // given
        dictionaryController = new DocumentController(dictionaryInsertBatchService, mockFileHandler);

        Document document = mock(Document.class);
        when(document.getMimeType()).thenReturn("image/jpg");
        UpdateDocument updateDocument = UpdateDocument.builder().document(document).build();

        // when
        assertThatThrownBy(()->{
            dictionaryController.excelPush(updateDocument, null);
        }).isInstanceOf(IllegalArgumentException.class).message().contains("excel 파일만");
    }
    
    @Test
    void success() {
        // given
        when(mockFileHandler.extractExcelFile(any())).thenReturn(new File(getClass().getClassLoader().getResource("exceltest/dictionary_test.xlsx").getFile()));

        final ChatUser chatUser = new ChatUser(123l, "kim");

        DictionarySource source = new DictionarySource("wefijw", DictionarySource.Type.EXCEL, chatUser);
        given(mockDictionarySourceRepository.save(any())).willReturn(source);

        // when
        dictionaryController.excelPush(mockDocumentWithExcel("/excel_push"), chatUser);

        // then
        final List<Dictionary> result = memoryDictionaryRepository.findAll();
        assertThat(result.size()).isEqualTo(47);
    }

    private UpdateDocument mockDocumentWithExcel(String caption) {
        final String mockJson = excelDocumentJson(caption);
        final UpdateRequest updateRequest = jsonToUpdateRequest(mockJson);
        final UpdateDocument document = updateRequest.toDocument();
        return document;
    }
}