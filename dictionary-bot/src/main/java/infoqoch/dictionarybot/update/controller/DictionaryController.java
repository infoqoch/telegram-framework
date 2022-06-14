package infoqoch.dictionarybot.update.controller;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.service.DictionaryService;
import infoqoch.dictionarybot.update.controller.file.TelegramFileHandler;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.request.body.UpdateDocument;
import infoqoch.dictionarybot.update.resolver.mapper.UpdateRequestBodyParameterMapper;
import infoqoch.dictionarybot.update.resolver.mapper.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.entity.FilePath;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.FilePathRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.*;

@Slf4j
@Controller
@AllArgsConstructor
public class DictionaryController {
    private final DictionaryRepository dictionaryRepository;
    private final DictionaryService dictionaryService;
    private final TelegramFileHandler telegramFileHandler;

    @UpdateRequestMethodMapper(LOOKUP_WORD)
    public UpdateResponse lookupByWord(UpdateRequest updateRequest) {
        log.info("UpdateRequestMethodMapper : lookupByWord!");
        final List<Dictionary> result = dictionaryRepository.findByWord(updateRequest.getValue());
        return new UpdateResponse(SendType.MESSAGE, result.toString());
    }

    @UpdateRequestMethodMapper(HELP)
    public String help() {
        log.info("UpdateRequestMethodMapper : help!");
        return "요래 저래 쓰면 된단다!";
    }

    @UpdateRequestMethodMapper(UNKNOWN)
    public String unknown() {
        log.info("UpdateRequestMethodMapper : unknown");
        return help();
    }

    @UpdateRequestMethodMapper(EXCEL_PUSH)
    public String excelPush(@UpdateRequestBodyParameterMapper UpdateDocument document) {
        log.info("UpdateRequestMethodMapper : excel_push");

        final File file = telegramFileHandler.extractExcelFile(document);

        dictionaryService.saveExcel(file);

        return "good job!";
    }


//    TODO!
//    @UpdateRequestMethodMapper(LOOKUP_SENTENCE)
//    public String lookupBySentence(UpdateRequest request) {
//        StringBuilder sb = new StringBuilder();
//        return "LOOKUP_SENTENCE : " + request.getValue();
//    }
}
