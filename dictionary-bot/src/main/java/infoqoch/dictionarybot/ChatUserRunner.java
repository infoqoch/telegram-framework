package infoqoch.dictionarybot;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.service.LookupService;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.model.user.ChatUserRepository;
import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.SendRequest;
import infoqoch.dictionarybot.system.event.Events;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatUserRunner {
    private final ChatUserRepository chatUserRepository;
    private final LookupService lookupService;

    @Scheduled(cron = "0/5 * * * * *")
    public void hourlyDictionaryRun() {
        log.info("ChatUserRunner#hourlyDictionaryRun");
        // 기본적인 값이 있음을 상정한다.
        final Optional<Dictionary> random = lookupService.getRandom();
        if(random.isEmpty()){
            log.info("아직 없어ㅠ ");
            return;
        }

        Dictionary dictionary = random.get();

        List<ChatUser> chatUsers = chatUserRepository.findByHourlyAlarm(true);
        for (ChatUser chatUser : chatUsers) {

            if(chatUser.isLookupAllUsers()){
                Optional<Dictionary> myDictionary = lookupService.getRandom(chatUser);

                if(myDictionary.isEmpty())
                    Events.raise(Send.of(SendRequest.sendMessage(
                            chatUser.getChatId()
                            , new MarkdownStringBuilder().bold("=정시의 영어단어장!=").lineSeparator().plain("아직 등록한 사전이 없습니다!")
                    )));

                final Send send = Send.of(SendRequest.sendMessage(chatUser.getChatId(), new MarkdownStringBuilder().bold("=정시의 영어단어장!=").lineSeparator().append(myDictionary.get().toMarkdown())));
                Events.raise(send);
            }else {
                final Send send = Send.of(SendRequest.sendMessage(chatUser.getChatId(), new MarkdownStringBuilder().bold("=정시의 영어단어장!=").lineSeparator().append(dictionary.toMarkdown())));
                Events.raise(send);
            }
        }

    }
}
