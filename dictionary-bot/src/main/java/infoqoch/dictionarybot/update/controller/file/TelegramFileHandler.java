package infoqoch.dictionarybot.update.controller.file;

import infoqoch.dictionarybot.update.request.body.UpdateDocument;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.FilePath;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.FilePathRequest;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@RequiredArgsConstructor
public class TelegramFileHandler {
    private final TelegramBot telegramBot;
    private final TelegramBotProperties telegramBotProperties;

    public File extractExcelFile(UpdateDocument document) {
        validExcelFile(document);
        final Response<FilePath> path = telegramBot.file().path(new FilePathRequest(document.getChat().getId(), document.getDocument().getFileId()));
        return getFileByUrl(path.getResult().getFilePath());
    }

    private void validExcelFile(UpdateDocument document) {
        if(!document.getDocument().getMimeType().contains("sheet")) throw new IllegalArgumentException("excel 파일만 가능합니다!");
    }

    private File getFileByUrl(String filePath) {
        try{
            InputStream inputStream = new URL(telegramBotProperties.getUrl().getFile()+"/"+filePath).openStream();
            File file = File.createTempFile("abc",".xlsx");
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return file;
        }catch (IOException e){
            throw new IllegalStateException(e);
        }
    }
}
