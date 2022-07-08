package infoqoch.dictionarybot.update.controller;

import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.update.controller.resolver.UpdateRequestMethodMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.HELP;
import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.UNKNOWN;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DictionaryController {
    private final DictionaryRepository dictionaryRepository;

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

}
