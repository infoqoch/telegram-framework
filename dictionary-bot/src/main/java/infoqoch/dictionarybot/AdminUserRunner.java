package infoqoch.dictionarybot;

import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.model.user.ChatUserRepository;
import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.SendRequest;
import infoqoch.dictionarybot.send.SendType;
import infoqoch.dictionarybot.send.repository.SendJpaRepository;
import infoqoch.dictionarybot.system.event.Events;
import infoqoch.dictionarybot.update.log.UpdateLog;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AdminUserRunner {
    private final ChatUserRepository chatUserRepository;
    private final SendJpaRepository sendRepository;
    private long LAST_SEND_NO;

    public AdminUserRunner(ChatUserRepository chatUserRepository, SendJpaRepository sendRepository) {
        this.chatUserRepository = chatUserRepository;
        this.sendRepository = sendRepository;
        setupLastSendNo();
    }

    private void setupLastSendNo() {
        final Long maxNo = sendRepository.maxNo();
        if(maxNo==null){
            LAST_SEND_NO = 0l;
        }else {
            LAST_SEND_NO = maxNo;
        }
    }

    @Scheduled(fixedDelay = 1000l)
    public void run() {
        final List<Send> serverErrorSent = sendRepository.findByNoGreaterThanAndRequestSendType(LAST_SEND_NO, SendType.SERVER_ERROR);
        if(serverErrorSent.size()==0) return;

        final MarkdownStringBuilder message = convertAdminAlert(serverErrorSent);

        final List<ChatUser> admins = chatUserRepository.findByRole(ChatUser.Role.ADMIN);
        for (ChatUser admin : admins) {

            requestSending(SendRequest.send(admin.getChatId(), SendType.ADMIN_ALERT, message, null));
        }
    }

    private MarkdownStringBuilder convertAdminAlert(List<Send> serverErrorSent) {
        final MarkdownStringBuilder result = new MarkdownStringBuilder();
        result.bold("=어드민 경고 알림!=").lineSeparator();

        for (Send send : serverErrorSent) {
            result
                    .plain("send no : ").plain(String.valueOf(send.getNo())).lineSeparator()
                    .plain("sent message : [").append(send.getRequest().getMessage()).plain("]").lineSeparator()
                    .append(errorMessage(send.getErrorCode(), send.getErrorMessage()))
                    .append(causedByUpdate(send.getUpdateLog()))
                    .lineSeparator();


        }
        return result;
    }

    private MarkdownStringBuilder errorMessage(String errorCode, String errorMessage) {
        return new MarkdownStringBuilder()
                .italic("error code : ").italic(errorCode==null?"":errorCode).lineSeparator()
                .plain("  -> message : ").plain(errorMessage==null?"":errorMessage).lineSeparator();
    }

    private MarkdownStringBuilder causedByUpdate(UpdateLog updateLog) {
        if(updateLog==null) return null;

        return new MarkdownStringBuilder()
                .italic("caused by update id : ").italic(String.valueOf(updateLog.getUpdateId())).lineSeparator()
                .plain("  -> command : ").plain(updateLog.getUpdateCommand().toString()).plain(" : ").plain(updateLog.getUpdateValue()==null?"":updateLog.getUpdateValue()).lineSeparator();


    }

    private void requestSending(SendRequest sendRequest) {
        final Send send = Send.of(sendRequest);
        Events.raise(send);
    }
}
