package infoqoch.dictionarybot.update.response;

import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.ToString;

@ToString
public class UpdateResponse {
    private final SendType type;
    private final String document;
    private final MarkdownStringBuilder message;

    public UpdateResponse(SendType type, MarkdownStringBuilder message) {
        this.type= type;
        this.message = message;
        this.document = document();
    }

    public UpdateResponse(SendType type, MarkdownStringBuilder message, String document) {
        this.type = type;
        this.message = message;
        this.document = document;
    }

    public SendType type() {
        return type;
    }

    public MarkdownStringBuilder message(){
        return message;
    }

    public String document(){
        return document;
    }
}
