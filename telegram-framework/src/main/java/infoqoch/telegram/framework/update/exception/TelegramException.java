package infoqoch.telegram.framework.update.exception;

import infoqoch.telegram.framework.update.response.SendType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

import java.util.Optional;

public interface TelegramException {
    Optional<MarkdownStringBuilder> response();

    static Optional<TelegramException> checkIfCausedByTelegramException(Throwable e) {
        if(e instanceof TelegramException) return Optional.of((TelegramException)e);

        if(e.getCause() != null) return checkIfCausedByTelegramException(e.getCause());

        return Optional.empty();
    }

    default SendType resolveErrorType() {
        return this instanceof TelegramClientException ? SendType.CLIENT_ERROR: SendType.SERVER_ERROR;
    }
}
