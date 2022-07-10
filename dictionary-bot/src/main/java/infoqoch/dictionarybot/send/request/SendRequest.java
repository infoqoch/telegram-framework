package infoqoch.dictionarybot.send.request;

import infoqoch.dictionarybot.send.SendType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import infoqoch.telegrambot.util.NotEscapedMSBException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class SendRequest {
    private Long chatId;

    @Enumerated(EnumType.STRING)
    private SendType sendType;

    @Convert(converter=MarkdownStringBuilderConverter.class)
    @Column(columnDefinition = "varchar2(10000)")
    private MarkdownStringBuilder message;

    private String document;

    public SendRequest(Long chatId, SendType sendType, MarkdownStringBuilder msb) {
        this.chatId = chatId;
        this.sendType = sendType;
        this.message = msb;
        this.document = null;
    }

    public SendRequest(Long chatId, SendType sendType, String document, MarkdownStringBuilder msb) {
        this.chatId = chatId;
        this.sendType = sendType;
        this.document = document;
        this.message = msb;
    }

    public SendType sendType() {
        return sendType;
     }

    public MarkdownStringBuilder message() {
        return message;
    }

    public Long chatId() {
        return chatId;
    }

    public String document() {
        return document;
    }

    public static class MarkdownStringBuilderConverter implements AttributeConverter<MarkdownStringBuilder, String> {
        @Override
        public String convertToDatabaseColumn(MarkdownStringBuilder markdownStringBuilder) {
            return markdownStringBuilder.toString();
        }

        @Override
        public MarkdownStringBuilder convertToEntityAttribute(String s) {
            try {
                return new MarkdownStringBuilder().notEscapedTest(s);
            } catch (NotEscapedMSBException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
