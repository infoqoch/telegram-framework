package infoqoch.telegram.framework.update.exception;

import infoqoch.telegram.framework.update.response.ResponseType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

public class TelegramServerException extends TelegramException{
    public TelegramServerException() {
        this(null, null, null);
    }

    public TelegramServerException(String message) {
        this(null, message, null);
    }

    public TelegramServerException(Throwable cause) {
        this(null, null, cause);
    }

    public TelegramServerException(String message, Throwable cause) {
        this(null, message, cause);
    }

    public TelegramServerException(MarkdownStringBuilder response, String message) {
        super(response, message, null);
    }

    public TelegramServerException(MarkdownStringBuilder response, String message, Throwable cause) {
        super(response, message, cause);
    }

    @Override
    public ResponseType resolveErrorType() {
        return ResponseType.SERVER_ERROR;
    }
}
