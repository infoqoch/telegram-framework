package infoqoch.dictionarybot.send.request;

import infoqoch.dictionarybot.send.SendType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.ToString;

@ToString
public class SendRequest {
    private final Long chatId;
    private final SendType sendType;
    private final MarkdownStringBuilder message;

    private final String document;

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
}
