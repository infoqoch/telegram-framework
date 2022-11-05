package infoqoch.telegram.framework.update.exception;

import infoqoch.telegram.framework.update.response.SendType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

import java.util.Optional;

public class TelegramException extends RuntimeException {
    Optional<MarkdownStringBuilder> response;

    public TelegramException(String message) {
    }

    public TelegramException(String message, Throwable cause) {
    }

    public static Optional<TelegramException> checkIfCausedByTelegramException(Throwable e) {
        if(e instanceof TelegramException) return Optional.of((TelegramException)e);

        if(e.getCause() != null) return checkIfCausedByTelegramException(e.getCause());

        return Optional.empty();
    }

    public Optional<MarkdownStringBuilder> response(){
        return response;
    }

    public SendType resolveErrorType() {
        return SendType.ERROR;
    }
}
