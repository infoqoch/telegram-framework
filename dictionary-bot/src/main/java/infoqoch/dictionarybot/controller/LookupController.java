package infoqoch.dictionarybot.controller;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.repository.LookupRepository;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.telegram.framework.update.request.UpdateRequestCommandAndValue;
import infoqoch.telegram.framework.update.resolver.UpdateRequestMethodMapper;
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

    @UpdateRequestMethodMapper({"w", "ㄷ"})
    public List<Dictionary> lookupByWord(UpdateRequestCommandAndValue updateRequestCommandAndValue, ChatUser chatUser) {
        log.info("UpdateRequestMethodMapper : lookupByWord!");
        return lookupRepository.lookup(10, 0, updateRequestCommandAndValue.getValue(), chatUser, LookupRepository.FindBy.WORD);
    }

    @UpdateRequestMethodMapper({"s", "ㅁ"})
    public List<Dictionary> lookupBySentence(UpdateRequestCommandAndValue updateRequestCommandAndValue, ChatUser chatUser) {
        log.info("UpdateRequestMethodMapper : lookupBySentence!");
        return lookupRepository.lookup(10, 0, updateRequestCommandAndValue.getValue(), chatUser, LookupRepository.FindBy.SENTENCE);
    }

    @UpdateRequestMethodMapper({"d", "ㅈ"})
    public List<Dictionary> lookupByDefinition(UpdateRequestCommandAndValue updateRequestCommandAndValue, ChatUser chatUser) {
        log.info("UpdateRequestMethodMapper : lookupByDefinition!");
        return lookupRepository.lookup(10, 0, updateRequestCommandAndValue.getValue(), chatUser, LookupRepository.FindBy.DEFINITION);
    }

    @UpdateRequestMethodMapper({"f", "ㅍ", "*"})
    public List<Dictionary> lookupByFullSearch(UpdateRequestCommandAndValue updateRequestCommandAndValue, ChatUser chatUser) {
        log.info("UpdateRequestMethodMapper : lookupByFullSearch!");
        return lookupRepository.lookup(10, 0, updateRequestCommandAndValue.getValue(), chatUser
                , LookupRepository.FindBy.WORD, LookupRepository.FindBy.DEFINITION, LookupRepository.FindBy.SENTENCE);
    }
}
