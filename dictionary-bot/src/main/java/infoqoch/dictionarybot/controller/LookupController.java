package infoqoch.dictionarybot.controller;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.repository.LookupRepository;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.telegram.framework.update.UpdateRequestMapper;
import infoqoch.telegram.framework.update.request.UpdateRequestCommandAndValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LookupController {

    private final LookupRepository lookupRepository;

    @UpdateRequestMapper({"w", "ㄷ"})
    public List<Dictionary> lookupByWord(UpdateRequestCommandAndValue updateRequestCommandAndValue, ChatUser chatUser) {
        log.info("UpdateRequestMapper : lookupByWord!");
        return lookupRepository.lookup(10, 0, updateRequestCommandAndValue.getValue(), chatUser, LookupRepository.FindBy.WORD);
    }

    @UpdateRequestMapper({"s", "ㅁ"})
    public List<Dictionary> lookupBySentence(UpdateRequestCommandAndValue updateRequestCommandAndValue, ChatUser chatUser) {
        log.info("UpdateRequestMapper : lookupBySentence!");
        return lookupRepository.lookup(10, 0, updateRequestCommandAndValue.getValue(), chatUser, LookupRepository.FindBy.SENTENCE);
    }

    @UpdateRequestMapper({"d", "ㅈ"})
    public List<Dictionary> lookupByDefinition(UpdateRequestCommandAndValue updateRequestCommandAndValue, ChatUser chatUser) {
        log.info("UpdateRequestMapper : lookupByDefinition!");
        return lookupRepository.lookup(10, 0, updateRequestCommandAndValue.getValue(), chatUser, LookupRepository.FindBy.DEFINITION);
    }

    @UpdateRequestMapper({"f", "ㅍ", "*"})
    public List<Dictionary> lookupByFullSearch(UpdateRequestCommandAndValue updateRequestCommandAndValue, ChatUser chatUser) {
        log.info("UpdateRequestMapper : lookupByFullSearch!");
        return lookupRepository.lookup(10, 0, updateRequestCommandAndValue.getValue(), chatUser
                , LookupRepository.FindBy.WORD, LookupRepository.FindBy.DEFINITION, LookupRepository.FindBy.SENTENCE);
    }
}
