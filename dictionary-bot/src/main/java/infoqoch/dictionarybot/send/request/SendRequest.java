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

    public SendRequest(Long chatId, SendType type, Object body) {
        this.chatId = chatId;
        this.type = type;
        this.message = bodyResolver(body);
        this.document = null;
    }

    public SendRequest(Long chatId, SendType type, String document, Object body) {
        this.chatId = chatId;
        this.type = type;
        this.document = document;
        this.message = bodyResolver(body);
    }

    private MarkdownStringBuilder bodyResolver(Object body) {
        if(body instanceof MarkdownStringBuilder)
            return (MarkdownStringBuilder) body;
        if(body instanceof String)
            return new MarkdownStringBuilder().plain((String) body);
        throw new IllegalArgumentException("not support send request body type");
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
