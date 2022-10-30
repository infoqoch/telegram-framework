package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.send.repository.SendRepository;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static infoqoch.dictionarybot.send.SendType.SERVER_ERROR;

@Profile("scheduler")
@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class SendRequestEventListener {
    private final SendRepository repository;
    private final TelegramBot telegramBot;

    @EventListener(Send.class)
    public void handle(Send send) {
        repository.save(send);
        try {
            send.sending(telegramBot.send());
        } catch (Exception e) {
            log.error("[error : {}], ", "DictionarySendRunner", e);
            final SendRequest errorSendRequest = SendRequest.send(send.getRequest().getChatId(), SERVER_ERROR, new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (2)"), null);
            final Send errorSend = Send.of(errorSendRequest);
            repository.save(errorSend);
            errorSend.sending(telegramBot.send());
        }
    }
}