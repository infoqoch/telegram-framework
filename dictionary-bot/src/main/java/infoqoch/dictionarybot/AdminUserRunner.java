package infoqoch.dictionarybot;

import infoqoch.dictionarybot.log.send.SendLog;
import infoqoch.dictionarybot.log.send.service.SendRunnerService;
import infoqoch.dictionarybot.log.update.UpdateLog;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.model.user.ChatUserRepository;
import infoqoch.telegram.framework.update.event.Events;
import infoqoch.telegram.framework.update.response.ResponseType;
import infoqoch.telegram.framework.update.send.Send;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Scheduled(cron = "0 0/10 * * * *")
    @Transactional
    public void run() {
        final List<SendLog> serverErrorSent = sendRunnerService.findByNoGreaterThanAndResponseTypeForScheduler(LAST_SEND_NO, ResponseType.SERVER_ERROR);
        if(serverErrorSent.size()==0) return;

        upToDateLastSendNo(serverErrorSent);

        final MarkdownStringBuilder message = convertAdminAlert(serverErrorSent);

        final List<ChatUser> admins = chatUserRepository.findByRole(ChatUser.Role.ADMIN);
        for (ChatUser admin : admins) {
            Events.raise(Send.send(admin.getChatId(), ResponseType.MESSAGE, message, null));
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

    private void upToDateLastSendNo(List<SendLog> serverErrorSent) {
        for (SendLog sendLog : serverErrorSent)
            if(sendLog.getNo()>LAST_SEND_NO) LAST_SEND_NO = sendLog.getNo();
    }

    private MarkdownStringBuilder convertAdminAlert(List<SendLog> serverErrorSent) {
        final MarkdownStringBuilder result = new MarkdownStringBuilder();
        result.bold("=어드민 경고 알림!=").lineSeparator();

        for (SendLog sendLog : serverErrorSent) {
            result
                    .plain("send no : ").plain(String.valueOf(sendLog.getNo())).lineSeparator()
                    .plain("sent message : [").append(sendLog.getMessage()).plain("]").lineSeparator()
                    .append(errorMessage(sendLog.getErrorCode(), sendLog.getErrorMessage()))
                    .append(causedByUpdate(sendLog.getUpdateLog()))
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
                // .italic("caused by update id : ").italic(String.valueOf(updateLog.getUpdateId())).lineSeparator() // TODO 같은 값이 들어갈 경우 _italic__italic_ 형태가 되어 __ 가 정상적으로 인식되지 아니함.
                .italic("caused by update id : ").plain(" ").italic(String.valueOf(updateLog.getUpdateId())).lineSeparator()
                .plain("  -> command : ").plain(updateLog.getUpdateCommand().toString()).plain(" : ").plain(updateLog.getUpdateValue()==null?"":updateLog.getUpdateValue()).lineSeparator();
    }

//    private void requestSending(SendRequest sendRequest) {
//        final SendLog sendLog = SendLog.of(sendRequest);
//        Events.raise(sendLog);
//    }
}
