package infoqoch.dictionarybot.controller;

import infoqoch.dictionarybot.model.dictionary.DictionarySource;
import infoqoch.dictionarybot.model.dictionary.service.DictionaryInsertBatchService;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.telegram.framework.update.UpdateRequestMapper;
import infoqoch.telegram.framework.update.exception.TelegramClientException;
import infoqoch.telegram.framework.update.file.TelegramFileHandler;
import infoqoch.telegram.framework.update.request.body.UpdateDocument;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

import static infoqoch.dictionarybot.model.dictionary.DictionarySource.Type.EXCEL;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentController {
    private final DictionaryInsertBatchService dictionaryInsertBatchService;
    private final TelegramFileHandler telegramFileHandler;

    @UpdateRequestMapper({"excel push", "push", "replace"})
    public String excelPush(UpdateDocument document, ChatUser chatUser) {
        log.info("UpdateRequestMapper : excel_push");

        valid(document);

        final File file = telegramFileHandler.documentToFile(document);

        final DictionarySource source = new DictionarySource(document.getDocument().getFileId(), EXCEL, chatUser);

        final int saved = dictionaryInsertBatchService.saveExcel(file, source, chatUser).size();

        dictionaryInsertBatchService.deleteSourcesExclude(source, chatUser);

        return saved + "의 사전이 등록되었습니다!";
    }

    private void valid(UpdateDocument document) {
        if(!document.hasDocument())
            throw new TelegramClientException(new MarkdownStringBuilder().bold("파일을 첨부해야 합니다!").lineSeparator().command("excel", "help"), "document가 누락되었습니다.");

        if(!document.getDocument().getMimeType().contains("sheet"))
            throw new IllegalArgumentException("excel 파일만 가능합니다!");
    }

}
