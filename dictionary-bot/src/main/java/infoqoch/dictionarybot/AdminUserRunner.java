package infoqoch.dictionarybot;

import infoqoch.dictionarybot.log.UpdateLog;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.model.user.ChatUserRepository;
import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.SendRequest;
import infoqoch.dictionarybot.send.service.SendRunnerService;
import infoqoch.dictionarybot.system.event.Events;
import infoqoch.telegram.framework.update.response.SendType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Async
@Slf4j
@Component
public class AdminUserRunner {
    private final ChatUserRepository chatUserRepository;
    private final SendRunnerService sendRunnerService;
    private long LAST_SEND_NO;

    public AdminUserRunner(ChatUserRepository chatUserRepository, SendRunnerService sendRunnerService) {
        this.chatUserRepository = chatUserRepository;
        this.sendRunnerService = sendRunnerService;
        setupLastSendNo();
    }

    @Scheduled(fixedDelay = 100000)
    @Transactional
    public void run() {
        final List<Send> serverErrorSent = sendRunnerService.findByNoGreaterThanAndRequestSendTypeForScheduler(LAST_SEND_NO, SendType.SERVER_ERROR);
        if(serverErrorSent.size()==0) return;

        upToDateLastSendNo(serverErrorSent);

        final MarkdownStringBuilder message = convertAdminAlert(serverErrorSent);

        final List<ChatUser> admins = chatUserRepository.findByRole(ChatUser.Role.ADMIN);
        for (ChatUser admin : admins) {
            requestSending(SendRequest.send(admin.getChatId(), SendType.ADMIN_ALERT, message, null));
        }
    }

    private void setupLastSendNo() {
        final Long maxNo = sendRunnerService.maxNo();
        if(maxNo==null){
            LAST_SEND_NO = 0l;
        }else {
            LAST_SEND_NO = maxNo;
        }
    }

    private void upToDateLastSendNo(List<Send> serverErrorSent) {
        for (Send send : serverErrorSent)
            if(send.getNo()>LAST_SEND_NO) LAST_SEND_NO = send.getNo();
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

    private MarkdownStringBuilder errorMessage(int errorCode, String errorMessage) {
        return new MarkdownStringBuilder()
                .italic("error code : ").plain(errorCode==0?" ":String.valueOf(errorCode)).lineSeparator()
                .plain("  -> message : ").plain(errorMessage==null?" ":errorMessage).lineSeparator();
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
