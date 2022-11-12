package infoqoch.telegram.framework.update.exception;

import infoqoch.telegram.framework.update.response.ResponseType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

public class TelegramClientException extends TelegramException{
    public TelegramClientException() {
        this(null, null, null);
    }

    public TelegramClientException(String message) {
        this(null, message, null);
    }

    public TelegramClientException(Throwable cause) {
        this(null, null, cause);
    }

    public TelegramClientException(String message, Throwable cause) {
        this(null, message, cause);
    }

    public TelegramClientException(MarkdownStringBuilder response, String message) {
        super(response, message, null);
    }

    public TelegramClientException(MarkdownStringBuilder response, String message, Throwable cause) {
        super(response, message, cause);
    }

    @Override
    public ResponseType resolveErrorType() {
        return ResponseType.CLIENT_ERROR;
    }
}
