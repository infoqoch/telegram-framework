package infoqoch.dictionarybot.update.controller;

import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.update.controller.resolver.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.request.UpdateRequestMessage;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.HELP;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DictionaryController {
    private final DictionaryRepository dictionaryRepository;

    @UpdateRequestMethodMapper(HELP)
    public MarkdownStringBuilder help(UpdateRequestMessage message) {
        log.info("UpdateRequestMethodMapper : help!");

        throwEx(message);

        return new MarkdownStringBuilder()
                .bold("=== 사용방법 ===").lineSeparator()
                .plain("원하는 명령어를 입력하세요!");
    }

    private void throwEx(UpdateRequestMessage message) {
        if(message.getValue().equalsIgnoreCase("throw new IllegalArgumentException"))
            throw new IllegalArgumentException("테스트 용 인자 오류 발생");
        if(message.getValue().equalsIgnoreCase("throw new IllegalStateException"))
            throw new IllegalStateException("테스트 용 서버 오류 발생");
    }
}
