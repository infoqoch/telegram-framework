package infoqoch.dictionarybot.log.send;

import infoqoch.dictionarybot.log.update.UpdateLog;
import infoqoch.telegram.framework.update.response.SendType;
import infoqoch.telegram.framework.update.send.Send;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import infoqoch.telegrambot.util.NotEscapedMSBException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


@ToString(exclude = "updateLog")
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "send")
@AllArgsConstructor @Builder
public class SendLog {
    @Id @GeneratedValue
    private Long no;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "update_log_no")
    private UpdateLog updateLog;

    private Long chatId;

    @Enumerated(EnumType.STRING)
    private SendType sendType;

    @Convert(converter=MarkdownStringBuilderConverter.class)
    @Column(columnDefinition = "varchar(10000)")
    private MarkdownStringBuilder message;

    private String document;

    @Enumerated(EnumType.STRING)
    private Send.Status status;

    private int errorCode;
    private String errorMessage;

    public static SendLog of(Send send) {
        final SendLogBuilder builder = SendLog.builder()
                .updateLog(sendToUpdateLog(send))
                .status(send.getStatus())
                .sendType(send.getSendType())
                .chatId(send.getChatId());

        if(send.getResend().isPresent()){
            final Send resend = send.getResend().get();
            return builder
                    .message(resend.getMessage())
                    .document(resend.getDocument())
                    .errorCode(resend.getErrorCode())
                    .errorMessage(resend.getErrorMessage())
                    .build();
        }else{
            return builder
                    .message(send.getMessage())
                    .document(send.getDocument())
                    .errorCode(send.getErrorCode())
                    .errorMessage(send.getErrorMessage())
                    .build();
        }
    }

    private static UpdateLog sendToUpdateLog(Send send) {
        UpdateLog updateLog = null;
        if(send.getUpdateId().isPresent()){
            updateLog = UpdateLog.of(send.getUpdateRequest().get(), send.getUpdateResponse().get());
        }
        return updateLog;
    }

    public static class MarkdownStringBuilderConverter implements AttributeConverter<MarkdownStringBuilder, String> {
        @Override
        public String convertToDatabaseColumn(MarkdownStringBuilder markdownStringBuilder) {
            return markdownStringBuilder.toString();
        }

        @Override
        public MarkdownStringBuilder convertToEntityAttribute(String s) {
            try {
                return new MarkdownStringBuilder().notEscapedText(s);
            } catch (NotEscapedMSBException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}