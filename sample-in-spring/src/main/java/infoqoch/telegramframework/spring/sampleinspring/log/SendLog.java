package infoqoch.telegramframework.spring.sampleinspring.log;

import infoqoch.telegram.framework.update.send.Send;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendLog {
    @Id @GeneratedValue
    private Long no;

    private Long chatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_log_no", nullable = true)
    private UpdateLog updateLog;

    private String message;
    private String document;

    public SendLog(Send send, UpdateLog updateLog) {
        this.chatId = send.getChatId();
        this.message = send.getMessage().toString();
        this.document = send.getDocument();
        this.updateLog = updateLog;
    }

    public static SendLog of(Send send, UpdateLog updateLog) {
        return new SendLog(send, updateLog);
    }
}
