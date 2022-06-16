package infoqoch.dictionarybot.update.controller;

import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.service.DictionaryService;
import infoqoch.dictionarybot.update.controller.file.TelegramFileHandler;
import infoqoch.dictionarybot.update.request.body.UpdateDocument;
import infoqoch.dictionarybot.update.resolver.mapper.UpdateRequestBodyParameterMapper;
import infoqoch.dictionarybot.update.resolver.mapper.UpdateRequestMethodMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.File;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.EXCEL_PUSH;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentController {
    private final DictionaryRepository dictionaryRepository;
    private final DictionaryService dictionaryService;
    private final TelegramFileHandler telegramFileHandler;

    @UpdateRequestMethodMapper(EXCEL_PUSH)
    public String excelPush(@UpdateRequestBodyParameterMapper UpdateDocument document) {
        log.info("UpdateRequestMethodMapper : excel_push");

        final File file = telegramFileHandler.extractExcelFile(document);

        dictionaryService.saveExcel(file);

        return "good job!";
    }
}
