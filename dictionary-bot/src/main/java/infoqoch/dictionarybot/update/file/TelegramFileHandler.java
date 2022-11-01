package infoqoch.dictionarybot.update.file;

import infoqoch.dictionarybot.system.properties.DictionaryProperties;
import infoqoch.dictionarybot.update.exception.TelegramServerException;
import infoqoch.dictionarybot.update.request.body.UpdateDocument;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.entity.FilePath;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.FilePathRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramFileHandler {
    private final TelegramBot telegramBot;
    private final DictionaryProperties dictionaryProperties;

    public File extractExcelFile(UpdateDocument document) {
        validExcelFile(document);
        final Response<FilePath> path = telegramBot.file().path(new FilePathRequest(document.getChat().getId(), document.getDocument().getFileId()));
        return getFileByUrl(createFileName(document), path.getResult().getFilePath());
    }

    private String createFileName(UpdateDocument document) {
        final Long chatId = document.getChat().getId();
        final long now = System.currentTimeMillis();
        return chatId + "_"+ fileNameExcludeExtension(document.getDocument().getFileName()) +"_"+now+".xlsx";
    }

    private String fileNameExcludeExtension(String fileName) {
        final int idx = fileName.lastIndexOf(".xlsx");
        return fileName.substring(0, idx);
    }

    private void validExcelFile(UpdateDocument document) {
        if(!document.getDocument().getMimeType().contains("sheet"))
            throw new IllegalArgumentException("excel 파일만 가능합니다!");
    }

    private File getFileByUrl(String fileName, String filePath) {
        try(InputStream inputStream = new URL(telegramBot.url().file()+"/"+filePath).openStream();){
            File directory = new File(dictionaryProperties.directory().excel());
            if(!directory.exists()) directory.mkdirs();
            File file = new File(directory, fileName);
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return file;
        }catch (IOException e){
            throw new TelegramServerException(e);
        }
    }
}
