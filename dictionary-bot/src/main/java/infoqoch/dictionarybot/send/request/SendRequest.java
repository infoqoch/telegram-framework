package infoqoch.dictionarybot.send.request;

import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.ToString;

@ToString
public class SendRequest {
    private final Long chatId;
    private final SendType type;
    private final MarkdownStringBuilder message;

    private final String document;

    public SendRequest(Long chatId, SendType type, MarkdownStringBuilder msb) {
        this.chatId = chatId;
        this.type = type;
        this.message = msb;
        this.document = null;
    }

    public SendRequest(Long chatId, SendType type, String document, MarkdownStringBuilder msb) {
        this.chatId = chatId;
        this.type = type;
        this.document = document;
        this.message = msb;
    }

    public SendType type() {
        return type;
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
}
