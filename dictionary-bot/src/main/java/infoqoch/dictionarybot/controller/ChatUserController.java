package infoqoch.dictionarybot.controller;

import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.model.user.ChatUserRepository;
import infoqoch.dictionarybot.system.properties.DictionaryProperties;
import infoqoch.telegram.framework.update.UpdateRequestMapper;
import infoqoch.telegram.framework.update.exception.TelegramClientException;
import infoqoch.telegram.framework.update.request.UpdateRequestCommandAndValue;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatUserController {
    private final ChatUserRepository chatUserRepository;
    private final DictionaryProperties dictionaryProperties;
    private final String LOOKUP_ALL_USERS = "lookup all users";
    private final String SHARE_MINE = "share mine";
    private final String HOURLY_ALARM = "hourly";
    private final String PROMOTION = "promotion";

    @UpdateRequestMapper(LOOKUP_ALL_USERS)
    public MarkdownStringBuilder lookupAllUsers(ChatUser chatUser, UpdateRequestCommandAndValue message) {
        log.info("UpdateRequestMapper : LOOKUP_ALL_USERS");
        chatUser.setLookupAllUsers(lookupAllUsers(message));
        return new MarkdownStringBuilder()
                .bold("정상적으로 변경되었습니다!").lineSeparator()
                .plain("모든 회원의 검색 여부 : ")
                .plain(message.getValue());
    }

    @UpdateRequestMapper(SHARE_MINE)
    public MarkdownStringBuilder shareMine(ChatUser chatUser, UpdateRequestCommandAndValue message) {
        log.info("UpdateRequestMapper : SHARE_MINE");
        chatUser.setShareMine(shareMine(message));
        return new MarkdownStringBuilder()
                .bold("정상적으로 변경되었습니다!").lineSeparator()
                .plain("사전 공개 여부 : ")
                .plain(message.getValue());
    }

    @UpdateRequestMapper(HOURLY_ALARM)
    public MarkdownStringBuilder hourlyAlarm(ChatUser chatUser, UpdateRequestCommandAndValue message) {
        log.info("UpdateRequestMapper : HOURLY_ALARM");
        chatUser.setHourlyAlarm(hourlyAlarm(message.getValue()));
        return new MarkdownStringBuilder()
                .bold("정상적으로 변경되었습니다!").lineSeparator()
                .plain("매시 알림 여부 : ").plain(message.getValue());
    }

    @UpdateRequestMapper(PROMOTION)
    public MarkdownStringBuilder promotionRole(ChatUser chatUser, UpdateRequestCommandAndValue message) {
        log.info("UpdateRequestMapper : PROMOTION_ROLE");
        if(matchesPromotionCode(message)){
            chatUser.changeRole(ChatUser.Role.ADMIN);
            return new MarkdownStringBuilder()
                    .bold("정상적으로 변경되었습니다!").lineSeparator()
                    .plain("현재 역할 : ").plain(chatUser.getRole().toString());
        }
        return new MarkdownStringBuilder("코드가 일치하지 않습니다.");
    }

    private boolean matchesPromotionCode(UpdateRequestCommandAndValue message) {
        return dictionaryProperties.user().promotionToAdmin().equals(message.getValue().trim().replaceAll("_", " "));
    }

    @UpdateRequestMapper({"status", "상태"})
    public MarkdownStringBuilder myStatus(ChatUser chatUser) {
        System.out.println("chatUser = " + chatUser);
        log.info("UpdateRequestMapper : SHARE_MINE");
        return new MarkdownStringBuilder()

                .bold("==나의 상태").italic(chatUser.getRole()==ChatUser.Role.ADMIN?"[ADMIN]":" ").bold("==").lineSeparator()
                .plain("모든 회원 검색 여부 : ").plain(booleanToYnValue(chatUser.isLookupAllUsers())).lineSeparator()
                .italic(" 수정 : ").command(LOOKUP_ALL_USERS, booleanToYnValue(!chatUser.isLookupAllUsers())).lineSeparator()
                .plain("사전 공개 여부 : ").plain(booleanToYnValue(chatUser.isShareMine())).lineSeparator()
                .italic(" 수정 : ").command(SHARE_MINE, booleanToYnValue(!chatUser.isShareMine())).lineSeparator()
                .plain("매시 사전 알람 여부 : ").plain(booleanToYnValue(chatUser.isHourlyAlarm())).lineSeparator()
                .italic(" 수정 : ").command(HOURLY_ALARM, booleanToYnValue(!chatUser.isHourlyAlarm())).lineSeparator()
                .plain("등록한 사전의 갯수 : ").plain(String.valueOf(chatUser.getDictionaries().size()))
                ;
    }

    private boolean hourlyAlarm(String value) {
        if(ynToBoolean(value, "Y")){
            return true;
        }else if(ynToBoolean(value, "N")){
            return false;
        }

        throw new TelegramClientException(
                new MarkdownStringBuilder().bold("=매시 사전 알람=").lineSeparator()
                        .plain("Y 혹은 N으로 응답합니다.").lineSeparator()
                        .command(HOURLY_ALARM, "Y").lineSeparator()
                        .command(HOURLY_ALARM, "N").lineSeparator()
                , "HOURLY_ALARM에 대한 응답값을 Y 혹은 N으로 입력하지 않았습니다."
        );
    }

    private boolean shareMine(UpdateRequestCommandAndValue message) {
        return hourlyAlarm(message.getValue());
    }

    private boolean lookupAllUsers(UpdateRequestCommandAndValue message) {
        if(ynToBoolean(message.getValue(), "Y")){
            return true;
        }else if(ynToBoolean(message.getValue(), "N")){
            return false;
        }

        throw new TelegramClientException(
                new MarkdownStringBuilder().bold("=모든 회원의 사전 검색 여부=").lineSeparator()
                        .plain("Y 혹은 N으로 응답합니다.").lineSeparator()
                        .command(LOOKUP_ALL_USERS, "Y").lineSeparator()
                        .command(LOOKUP_ALL_USERS, "N").lineSeparator()
                , "LOOKUP_ALL_USERS에 대한 응답값을 Y 혹은 N으로 입력하지 않았습니다."
        );
    }

    private boolean ynToBoolean(String target, String expect) {
        return target.trim().equalsIgnoreCase(expect);
    }

    private String booleanToYnValue(boolean target) {
        return target?"Y":"N";
    }
}