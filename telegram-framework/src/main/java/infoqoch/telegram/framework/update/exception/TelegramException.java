package infoqoch.telegram.framework.update.exception;

import infoqoch.telegram.framework.update.response.SendType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

import java.util.Optional;

public class TelegramException extends RuntimeException {
    private final Optional<MarkdownStringBuilder> response;

    public TelegramException(MarkdownStringBuilder msb, String message, Throwable cause) {
        super(message, cause);
        this.response = Optional.ofNullable(msb);
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
