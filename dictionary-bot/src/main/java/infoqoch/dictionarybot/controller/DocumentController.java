package infoqoch.dictionarybot.controller;

import infoqoch.dictionarybot.model.dictionary.DictionarySource;
import infoqoch.dictionarybot.model.dictionary.service.DictionaryInsertBatchService;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.telegram.framework.update.UpdateRequestMethodMapper;
import infoqoch.telegram.framework.update.exception.TelegramClientException;
import infoqoch.telegram.framework.update.file.TelegramFileHandler;
import infoqoch.telegram.framework.update.request.body.UpdateDocument;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

import static infoqoch.dictionarybot.model.dictionary.DictionarySource.Type.EXCEL;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class DocumentController {
    private final DictionaryInsertBatchService dictionaryInsertBatchService;
    private final TelegramFileHandler telegramFileHandler;

    @UpdateRequestMethodMapper({"excel push", "push", "replace"})
    public String excelPush(UpdateDocument document, ChatUser chatUser) {
        log.info("UpdateRequestMethodMapper : excel_push");

        if(!document.hasDocument())
            throw new TelegramClientException(new MarkdownStringBuilder().bold("파일을 첨부해야 합니다!").lineSeparator().command("excel", "help"), "document가 누락되었습니다.");

        final File file = telegramFileHandler.extractExcelFile(document);

        final DictionarySource source = new DictionarySource(document.getDocument().getFileId(), EXCEL, chatUser);

        final int saved = dictionaryInsertBatchService.saveExcel(file, source, chatUser).size();

        dictionaryInsertBatchService.deleteSourcesExclude(source, chatUser);

        return saved + "의 사전이 등록되었습니다!";
    }
}
