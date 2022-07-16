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

    @Scheduled(cron = "0 0 * * * *")
    public void hourlyDictionaryRun() {
        final Optional<Dictionary> random = lookupService.getRandom();
        if (existsDictionary(random)) return;
        sending(random.get());
    }

    private boolean existsDictionary(Optional<Dictionary> random) {
        if(random.isEmpty()){
            log.info("등록된 사전이 없습니다. 스케줄러가 동작하지 않습니다.");
            return true;
        }
        return false;
    }

    private void sending(Dictionary dictionary) {
        List<ChatUser> chatUsers = chatUserRepository.findByHourlyAlarm(true);
        for (ChatUser chatUser : chatUsers) {
            sendingEachChatUser(chatUser, dictionary);
        }
    }

    private void sendingEachChatUser(ChatUser chatUser, Dictionary dictionary) {
        if(chatUser.isLookupAllUsers()){
            sendingDictionary(dictionary, chatUser);
        } else {
            Optional<Dictionary> myDictionary = lookupService.getRandom(chatUser);
            if(myDictionary.isPresent()){
                sendingMyDictionary(chatUser, myDictionary);
            }else{
                sendingNoResult(chatUser);
            }
        }
    }

    private void sendingDictionary(Dictionary dictionary, ChatUser chatUser) {
        final Send send = Send.of(SendRequest.sendMessage(chatUser.getChatId(), msgHeader().append(dictionary.toMarkdown())));
        Events.raise(send);
    }

    private void sendingMyDictionary(ChatUser chatUser, Optional<Dictionary> myDictionary) {
        Events.raise(Send.of(SendRequest.sendMessage(
                chatUser.getChatId()
                , msgHeader().append(myDictionary.get().toMarkdown())
        )));
    }

    private void sendingNoResult(ChatUser chatUser) {
        Events.raise(Send.of(SendRequest.sendMessage(
                chatUser.getChatId()
                , msgHeader().plain("아직 등록한 사전이 없습니다!")
        )));
    }

    private MarkdownStringBuilder msgHeader() {
        return new MarkdownStringBuilder().bold("=정시의 영어단어장!=").lineSeparator();
    }
}
