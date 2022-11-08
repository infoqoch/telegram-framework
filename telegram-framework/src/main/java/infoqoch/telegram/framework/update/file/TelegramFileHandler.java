package infoqoch.telegram.framework.update.file;

import infoqoch.telegram.framework.update.exception.TelegramServerException;
import infoqoch.telegram.framework.update.request.body.UpdateDocument;
import infoqoch.telegram.framework.update.util.TelegramProperties;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.TelegramFile;
import infoqoch.telegrambot.bot.entity.FilePath;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.FilePathRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Slf4j
public class TelegramFileHandler {
    private final TelegramFile telegramFile;
    private final String telegramFileCloudBaseUrl;
    private final String localDirectoryPath;

    public TelegramFileHandler(TelegramBot telegramBot, TelegramProperties telegramProperties) {
        this.telegramFile = telegramBot.file();
        this.telegramFileCloudBaseUrl = telegramBot.url().file();
        this.localDirectoryPath = telegramProperties.fileUploadPath();
    }

    public File documentToFile(UpdateDocument document) {
        final File target = new File(directory(), createFileName(document));

        transferUrlToFile(getFileUrl(document), target);

        return target;
    }

    private String createFileName(UpdateDocument document) {
        return document.getChat().getId() + "_" + LocalDateTime.now() + "_" + document.getDocument().getFileName();
    }

    private File directory() {
        File directory = new File(localDirectoryPath);
        if(!directory.exists()) directory.mkdirs();
        return directory;
    }

    private String getFileUrl(UpdateDocument document) {
        final Response<FilePath> path = telegramFile.path(new FilePathRequest(document.getChat().getId(), document.getDocument().getFileId()));
        return telegramFileCloudBaseUrl + "/" + path.getResult().getFilePath();
    }

    private void transferUrlToFile(String fileUrl, File file) {
        try(InputStream inputStream = new URL(fileUrl).openStream();){
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new TelegramServerException(e);
        }
    }
}
