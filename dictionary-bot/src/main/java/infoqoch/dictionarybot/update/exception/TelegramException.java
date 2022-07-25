package infoqoch.dictionarybot.update.exception;

import infoqoch.dictionarybot.send.SendType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

import java.util.Optional;

import static infoqoch.dictionarybot.send.SendType.CLIENT_ERROR;
import static infoqoch.dictionarybot.send.SendType.SERVER_ERROR;

public interface TelegramException {
    Optional<MarkdownStringBuilder> response();

    static Optional<TelegramException> checkIfCausedByTelegramException(Throwable e) {
        if(e instanceof TelegramException) return Optional.of((TelegramException)e);

        if(e.getCause() != null) return checkIfCausedByTelegramException(e.getCause());

        return Optional.empty();
    }

    default SendType resolveErrorType() {
        return this instanceof TelegramClientException ? CLIENT_ERROR: SERVER_ERROR;
    }
}
