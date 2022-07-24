package infoqoch.dictionarybot.telegram;

import infoqoch.dictionarybot.update.controller.file.TelegramFileHandler;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.request.body.UpdateDocument;
import infoqoch.telegrambot.bot.DefaultTelegramBotFactory;
import infoqoch.telegrambot.bot.TelegramBot;
import org.junit.jupiter.api.Test;

import java.io.File;

import static infoqoch.dictionarybot.mock.data.MockUpdate.excelDocumentJson;
import static infoqoch.dictionarybot.mock.data.MockUpdate.jsonToUpdateWrapper;
import static org.assertj.core.api.Assertions.assertThat;

class TelegramFileHandlerTest {

    // @Disabled("실제 telegram bot 과 통신함")
    @Test
    void handel_excel_file_out_of_telegram_cloud(){
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
        final UpdateRequest updateRequest = jsonToUpdateWrapper(mockJson);
        final UpdateDocument document = updateRequest.toDocument();
        return document;
    }

}