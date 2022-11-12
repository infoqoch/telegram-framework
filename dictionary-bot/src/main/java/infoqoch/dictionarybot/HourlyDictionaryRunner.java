package infoqoch.dictionarybot;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContentMarkdownPrinter;
import infoqoch.dictionarybot.model.dictionary.repository.LookupRepository;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.model.user.ChatUserRepository;
import infoqoch.telegram.framework.update.event.Events;
import infoqoch.telegram.framework.update.send.Send;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class HourlyDictionaryRunner {
    private final ChatUserRepository chatUserRepository;
    private final LookupRepository repository;

    @Scheduled(cron = "0 0/30 7-22 * * *")
    @Transactional
    public void hourlyDictionaryRun() {
        log.info("HourlyDictionaryRunner#hourlyDictionaryRun");
        final Optional<Dictionary> publicDictionary = findPublicRandomDictionary();

        if (existsDictionary(publicDictionary)) return;

        List<ChatUser> chatUsers = hourlyDictionarySubscribers();

        sending(chatUsers, publicDictionary.get());
    }

    private Optional<Dictionary> findPublicRandomDictionary() {
        return repository.getRandom();
    }

    private boolean existsDictionary(Optional<Dictionary> random) {
        if(random.isEmpty()){
            log.info("등록된 사전이 없습니다. 스케줄러가 동작하지 않습니다.");
            return true;
        }
        return false;
    }

    private List<ChatUser> hourlyDictionarySubscribers() {
        return chatUserRepository.findByHourlyAlarm(true);
    }

    private void sending(List<ChatUser> chatUsers, Dictionary dictionary) {
        for (ChatUser chatUser : chatUsers) {
            if(chatUser.isLookupAllUsers()){
                sendingDictionary(chatUser, dictionary);
            }else{
                sendingPrivateDictionary(chatUser);
            }
        }
    }

    private void sendingDictionary(ChatUser chatUser, Dictionary dictionary) {
        Events.raise(Send.sendMessage(chatUser.getChatId(), msgHeader().append(new DictionaryContentMarkdownPrinter(dictionary).toMarkdown())));
    }

    private MarkdownStringBuilder msgHeader() {
        return new MarkdownStringBuilder().bold("=정시의 영어단어장!=").lineSeparator();
    }

    private void sendingPrivateDictionary(ChatUser chatUser) {
        findPrivateRandomDictionary(chatUser).ifPresentOrElse(
                privateDictionary -> sendingDictionary(chatUser, privateDictionary)
                , () -> sendingNoResult(chatUser)
        );
    }


    private Optional<Dictionary> findPrivateRandomDictionary(ChatUser chatUser) {
        return repository.getRandom(chatUser);
    }

    private void sendingNoResult(ChatUser chatUser) {
        Events.raise(Send.sendMessage(chatUser.getChatId(), msgHeader().plain("아직 등록한 사전이 없습니다!")));
    }
}
