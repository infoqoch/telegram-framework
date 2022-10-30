package infoqoch.dictionarybot.update.exception;

import infoqoch.telegrambot.util.MarkdownStringBuilder;

import java.util.Optional;

public class TelegramServerException extends RuntimeException implements TelegramException {
    private final MarkdownStringBuilder response;

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

    public TelegramServerException(MarkdownStringBuilder response, String message, Throwable cause) {
        super(message, cause);
        this.response = response;
    }

    @Override
    public Optional<MarkdownStringBuilder> response(){
        return Optional.ofNullable(response);
    }
}
