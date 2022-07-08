package infoqoch.dictionarybot.update.controller;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.service.LookupService;
import infoqoch.dictionarybot.update.controller.resolver.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.request.UpdateRequestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LookupController {
    private final LookupService lookupService;
    @UpdateRequestMethodMapper(LOOKUP_WORD)
    public List<Dictionary> lookupByWord(UpdateRequestMessage updateRequestMessage) {
        log.info("UpdateRequestMethodMapper : lookupByWord!");
        return lookupService.word(updateRequestMessage.getValue(), 10, 0);
    }

    @UpdateRequestMethodMapper(LOOKUP_SENTENCE)
    public List<Dictionary> lookupBySentence(UpdateRequestMessage updateRequestMessage) {
        log.info("UpdateRequestMethodMapper : lookupBySentence!");
        return lookupService.sentence(updateRequestMessage.getValue());
    }

    @UpdateRequestMethodMapper(LOOKUP_DEFINITION)
    public List<Dictionary> lookupByDefinition(UpdateRequestMessage updateRequestMessage) {
        log.info("UpdateRequestMethodMapper : lookupByDefinition!");
        return lookupService.definition(updateRequestMessage.getValue());
    }
}
