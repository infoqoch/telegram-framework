package infoqoch.dictionarybot.update.controller;

import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.model.user.ChatUserRepository;
import infoqoch.dictionarybot.update.controller.resolver.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.exception.TelegramClientException;
import infoqoch.dictionarybot.update.request.UpdateRequestMessage;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.LOOKUP_ALL_USERS;
import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.SHARE_MINE;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatUserController {
    private final ChatUserRepository chatUserRepository;

    @UpdateRequestMethodMapper(LOOKUP_ALL_USERS)
    public MarkdownStringBuilder lookupAllUsers(ChatUser chatUser, UpdateRequestMessage message) {
        log.info("UpdateRequestMethodMapper : LOOKUP_ALL_USERS");
        chatUser.setLookupAllUsers(lookupAllUsers(message));
        chatUserRepository.save(chatUser);
        return new MarkdownStringBuilder()
                .bold("정상적으로 변경되었습니다!").lineSeparator()
                .plain("모든 회원의 검색 여부 : ")
                .plain(message.getValue());
    }

    @UpdateRequestMethodMapper(SHARE_MINE)
    public MarkdownStringBuilder shareMine(ChatUser chatUser, UpdateRequestMessage message) {
        log.info("UpdateRequestMethodMapper : SHARE_MINE");
        chatUser.setShareMine(shareMine(message));
        chatUserRepository.save(chatUser);
        return new MarkdownStringBuilder()
                .bold("정상적으로 변경되었습니다!").lineSeparator()
                .plain("사전 공개 여부 : ")
                .plain(message.getValue());
    }

    private boolean shareMine(UpdateRequestMessage message) {
        if(message.getValue().trim().equalsIgnoreCase("Y")){
            return true;
        }else if(message.getValue().trim().equalsIgnoreCase("N")){
            return false;
        }

        throw new TelegramClientException(
                new MarkdownStringBuilder().bold("=나의 사전 공개 여부=").lineSeparator()
                        .plain("Y 혹은 N으로 응답합니다.").lineSeparator()
                        .command(SHARE_MINE.alias(), "Y").lineSeparator()
                        .command(SHARE_MINE.alias(), "N").lineSeparator()
                , "SHARE_MINE에 대한 응답값을 Y 혹은 N으로 입력하지 않았습니다."
        );
    }

    private boolean lookupAllUsers(UpdateRequestMessage message) {
        if(message.getValue().trim().equalsIgnoreCase("Y")){
            return true;
        }else if(message.getValue().trim().equalsIgnoreCase("N")){
            return false;
        }

        throw new TelegramClientException(
                new MarkdownStringBuilder().bold("=모든 회원의 사전 검색 여부=").lineSeparator()
                        .plain("Y 혹은 N으로 응답합니다.").lineSeparator()
                        .command(LOOKUP_ALL_USERS.alias(), "Y").lineSeparator()
                        .command(LOOKUP_ALL_USERS.alias(), "N").lineSeparator()
                , "LOOKUP_ALL_USERS에 대한 응답값을 Y 혹은 N으로 입력하지 않았습니다."
        );
    }
}
