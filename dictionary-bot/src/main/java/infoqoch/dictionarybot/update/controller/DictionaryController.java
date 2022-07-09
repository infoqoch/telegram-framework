package infoqoch.dictionarybot.update.controller;

import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.update.controller.resolver.UpdateRequestMethodMapper;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
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
    public MarkdownStringBuilder help() {
        log.info("UpdateRequestMethodMapper : help!");
        return new MarkdownStringBuilder()
                .bold("=== 사용방법 ===").lineSeparator()
                .plain("원하는 명령어를 입력하세요!");
    }

    @UpdateRequestMethodMapper(UNKNOWN)
    public MarkdownStringBuilder unknown() {
        log.info("UpdateRequestMethodMapper : unknown");
        return help();
    }
}
