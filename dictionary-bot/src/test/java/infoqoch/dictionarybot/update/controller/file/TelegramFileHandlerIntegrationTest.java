package infoqoch.dictionarybot.update.controller.file;

import infoqoch.dictionarybot.update.request.UpdateWrapper;
import infoqoch.dictionarybot.update.request.body.UpdateDocument;
import infoqoch.telegrambot.bot.DefaultTelegramBotFactory;
import infoqoch.telegrambot.bot.TelegramBot;
import org.junit.jupiter.api.Test;

import java.io.File;

import static infoqoch.dictionarybot.update.request.body.MockUpdateGenerate.excelDocumentJson;
import static infoqoch.dictionarybot.update.request.body.MockUpdateGenerate.jsonToUpdateWrapper;
import static org.assertj.core.api.Assertions.assertThat;

class TelegramFileHandlerIntegrationTest {

    @Test
    void test(){
        TelegramBot bot =  DefaultTelegramBotFactory.init("1959903402:AAFfvMCssvDcESLewCDvj5WZk83cbnIZ08o");
        TelegramFileHandler handler = new TelegramFileHandler(bot);

        final UpdateDocument document = mockDocumentWithExcel("/excel_push");
        final File file = handler.extractExcelFile(document);

        // then
        assertThat(file.exists()).isTrue();
        assertThat(file.length()).isEqualTo(document.getDocument().getFileSize());
    }

    private UpdateDocument mockDocumentWithExcel(String caption) {
        final String mockJson = excelDocumentJson(caption);
        final UpdateWrapper updateWrapper = jsonToUpdateWrapper(mockJson);
        final UpdateDocument document = updateWrapper.toDocument();
        return document;
    }

}