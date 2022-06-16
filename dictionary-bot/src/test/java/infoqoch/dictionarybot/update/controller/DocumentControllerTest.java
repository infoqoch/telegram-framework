package infoqoch.dictionarybot.update.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.repository.MemoryDictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.service.DictionaryService;
import infoqoch.dictionarybot.update.controller.file.TelegramFileHandler;
import infoqoch.dictionarybot.update.request.UpdateWrapper;
import infoqoch.dictionarybot.update.request.body.UpdateDocument;
import infoqoch.telegrambot.bot.DefaultTelegramBotFactory;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static infoqoch.dictionarybot.update.request.body.MockUpdateGenerate.excelDocumentJson;
import static infoqoch.dictionarybot.update.request.body.MockUpdateGenerate.jsonToUpdateWrapper;
import static org.assertj.core.api.Assertions.assertThat;

class DocumentControllerTest {
    DocumentController dictionaryController;
    DictionaryService dictionaryService;
    DictionaryRepository dictionaryRepository;

    @BeforeEach
    void setUp(){
        dictionaryRepository = new MemoryDictionaryRepository();
        dictionaryService = new DictionaryService(dictionaryRepository);

        TelegramBot bot =  DefaultTelegramBotFactory.init("1959903402:AAFfvMCssvDcESLewCDvj5WZk83cbnIZ08o");
        final TelegramBotProperties properties = TelegramBotProperties.defaultProperties("1959903402:AAFfvMCssvDcESLewCDvj5WZk83cbnIZ08o");
        TelegramFileHandler handler = new TelegramFileHandler(bot, properties);

        dictionaryController = new DocumentController(dictionaryRepository, dictionaryService, handler);
    }

    @Test
    void test() throws JsonProcessingException {
        // given
        final UpdateDocument document = mockDocumentWithExcel("/excel_push");

        // when
        dictionaryController.excelPush(document);

        // then
        assertThat(dictionaryRepository.findAll().size()).isEqualTo(47);
    }

    private UpdateDocument mockDocumentWithExcel(String caption) {
        final String mockJson = excelDocumentJson(caption);
        final UpdateWrapper updateWrapper = jsonToUpdateWrapper(mockJson);
        final UpdateDocument document = updateWrapper.toDocument();
        return document;
    }
}