package infoqoch.dictionarybot.update.controller;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.service.DictionaryService;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.resolver.mapper.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DictionaryController {
    private final DictionaryRepository dictionaryRepository;
    private final DictionaryService dictionaryService;

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

    @UpdateRequestMethodMapper(LOOKUP_WORD)
    public UpdateResponse lookupByWord(UpdateRequest updateRequest) {
        log.info("UpdateRequestMethodMapper : lookupByWord!");
        final List<Dictionary> result = dictionaryRepository.findByWord(updateRequest.getValue());
        return new UpdateResponse(SendType.MESSAGE, result.toString());
    }

    @UpdateRequestMethodMapper(LOOKUP_SENTENCE)
    public String lookupBySentence(UpdateRequest updateRequest) {
        log.info("UpdateRequestMethodMapper : lookupBySentence!");
        final List<Dictionary> result = dictionaryRepository.findBySentence(updateRequest.getValue());
        return result.toString();
    }

}
