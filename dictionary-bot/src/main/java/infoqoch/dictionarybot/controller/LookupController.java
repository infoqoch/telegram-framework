package infoqoch.dictionarybot.controller;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.repository.LookupRepository;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.update.resolver.UpdateRequestMethodMapper;
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
    private final LookupRepository lookupRepository;
    @UpdateRequestMethodMapper(LOOKUP_WORD)
    public List<Dictionary> lookupByWord(UpdateRequestMessage updateRequestMessage, ChatUser chatUser) {
        log.info("UpdateRequestMethodMapper : lookupByWord!");
        return lookupRepository.lookup(10, 0, updateRequestMessage.getValue(), chatUser, LookupRepository.FindBy.WORD);
    }

    @UpdateRequestMethodMapper(LOOKUP_SENTENCE)
    public List<Dictionary> lookupBySentence(UpdateRequestMessage updateRequestMessage, ChatUser chatUser) {
        log.info("UpdateRequestMethodMapper : lookupBySentence!");
        return lookupRepository.lookup(10, 0, updateRequestMessage.getValue(), chatUser, LookupRepository.FindBy.SENTENCE);
    }

    @UpdateRequestMethodMapper(LOOKUP_DEFINITION)
    public List<Dictionary> lookupByDefinition(UpdateRequestMessage updateRequestMessage, ChatUser chatUser) {
        log.info("UpdateRequestMethodMapper : lookupByDefinition!");
        return lookupRepository.lookup(10, 0, updateRequestMessage.getValue(), chatUser, LookupRepository.FindBy.DEFINITION);
    }

    @UpdateRequestMethodMapper({LOOKUP_FULL_SEARCH, UNKNOWN})
    public List<Dictionary> lookupByFullSearch(UpdateRequestMessage updateRequestMessage, ChatUser chatUser) {
        log.info("UpdateRequestMethodMapper : lookupByFullSearch!");
        return lookupRepository.lookup(10, 0, updateRequestMessage.getValue(), chatUser
                , LookupRepository.FindBy.WORD, LookupRepository.FindBy.DEFINITION, LookupRepository.FindBy.SENTENCE);
    }
}
