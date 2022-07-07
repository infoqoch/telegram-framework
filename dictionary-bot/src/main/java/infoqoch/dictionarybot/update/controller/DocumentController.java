package infoqoch.dictionarybot.update.controller;

import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.service.DictionaryInsertBatchService;
import infoqoch.dictionarybot.update.controller.file.TelegramFileHandler;
import infoqoch.dictionarybot.update.controller.resolver.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.request.body.UpdateDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.EXCEL_PUSH;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class DocumentController {
    private final DictionaryRepository dictionaryRepository;
    private final DictionaryInsertBatchService dictionaryInsertBatchService;
    private final TelegramFileHandler telegramFileHandler;

    @UpdateRequestMethodMapper(EXCEL_PUSH)
    public String excelPush(UpdateDocument document) {
        log.info("UpdateRequestMethodMapper : excel_push");

        final File file = telegramFileHandler.extractExcelFile(document);

        final int saved = dictionaryInsertBatchService.saveExcel(file);

        return saved + "의 사전이 등록되었습니다!";
    }
}
