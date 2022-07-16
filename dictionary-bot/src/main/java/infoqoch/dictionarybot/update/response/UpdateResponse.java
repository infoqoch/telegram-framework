package infoqoch.dictionarybot.update.response;

import infoqoch.dictionarybot.send.SendType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpdateResponse {
    private final SendType sendType;
    private final String document;
    private final MarkdownStringBuilder message;

    private UpdateResponse(SendType sendType, MarkdownStringBuilder message, String document) {
        this.sendType = sendType;
        this.message = message;
        this.document = document;
    }

    public static UpdateResponse message(MarkdownStringBuilder message){
        return new UpdateResponse(SendType.MESSAGE, message, null);
    }

    public static UpdateResponse document(MarkdownStringBuilder message, String document){
        return new UpdateResponse(SendType.DOCUMENT, message, document);
    }

    public static UpdateResponse send(SendType sendType, MarkdownStringBuilder message){
        return new UpdateResponse(sendType, message,null);
    }

    public MarkdownStringBuilder getMessage() {
        return new MarkdownStringBuilder().append(message);
    }
}
