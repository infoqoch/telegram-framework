package infoqoch.telegram.framework.update.response;

import infoqoch.telegram.framework.update.exception.TelegramException;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpdateResponse {
    private final ResponseType responseType;
    private final String document;
    private final MarkdownStringBuilder message;

    private UpdateResponse(ResponseType responseType, MarkdownStringBuilder message, String document) {
        this.responseType = responseType;
        this.message = message;
        this.document = document;
    }

    public static UpdateResponse voids(){
        return new UpdateResponse(ResponseType.VOID, null, null);
    }

    public static UpdateResponse message(MarkdownStringBuilder message){
        return new UpdateResponse(ResponseType.MESSAGE, message, null);
    }

    public static UpdateResponse document(MarkdownStringBuilder message, String document){
        return new UpdateResponse(ResponseType.DOCUMENT, message, document);
    }

    public static UpdateResponse send(ResponseType responseType, MarkdownStringBuilder message){
        return new UpdateResponse(responseType, message,null);
    }

    public static UpdateResponse error(TelegramException e, MarkdownStringBuilder defaultMessage){
        if (e.response().isPresent())
            return UpdateResponse.send(e.resolveErrorType(), e.response().get());

        return UpdateResponse.send(e.resolveErrorType(), defaultMessage);
    }

    public MarkdownStringBuilder getMessage() {
        return new MarkdownStringBuilder().append(message);
    }
}
