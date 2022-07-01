package infoqoch.dictionarybot.update.response;

import infoqoch.dictionarybot.send.SendType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.ToString;

@ToString
public class UpdateResponse {
    private final SendType sendType;
    private final String document;
    private final MarkdownStringBuilder message;

    public UpdateResponse(SendType sendType, MarkdownStringBuilder message) {
        this.sendType = sendType;
        this.message = message;
        this.document = document();
    }

    public UpdateResponse(SendType sendType, MarkdownStringBuilder message, String document) {
        this.sendType = sendType;
        this.message = message;
        this.document = document;
    }

    public SendType sendType() {
        return sendType;
    }

    public MarkdownStringBuilder message(){
        return message;
    }

    public String document(){
        return document;
    }
}
