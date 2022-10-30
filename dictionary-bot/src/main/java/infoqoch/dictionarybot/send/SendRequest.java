package infoqoch.dictionarybot.send;

import infoqoch.telegrambot.util.MarkdownStringBuilder;
import infoqoch.telegrambot.util.NotEscapedMSBException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// !! 불변객체이며 이를 유지해야 함.
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class SendRequest {
    private Long chatId;

    @Enumerated(EnumType.STRING)
    private SendType sendType;

    @Convert(converter=MarkdownStringBuilderConverter.class)
    @Column(columnDefinition = "varchar(10000)")
    private MarkdownStringBuilder message;

    private String document;

    // 생성자
    private SendRequest(Long chatId, SendType sendType, String document, MarkdownStringBuilder msb) {
        this.chatId = chatId;
        this.sendType = sendType;
        this.document = document;
        this.message = msb;
    }

    public SendRequest copy() {
        return new SendRequest(getChatId(), getSendType(), getDocument(), getMessage());
    }

    public static SendRequest sendMessage(long chatId, MarkdownStringBuilder msb) {
        return new SendRequest(chatId, SendType.MESSAGE, null, msb);
    }

    public static SendRequest sendDocument(Long chatId, String document, MarkdownStringBuilder msb) {
        return new SendRequest(chatId, SendType.DOCUMENT, document, msb);
    }

    public static SendRequest send(Long chatId, SendType sendType, MarkdownStringBuilder message, String document) {
        return new SendRequest(chatId, sendType, document, message);
    }

    // getter
    public MarkdownStringBuilder getMessage() {
        return new MarkdownStringBuilder().append(message);
    }

    // MarkdownStringBuilder 에 대한 JPA 컨버터
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
