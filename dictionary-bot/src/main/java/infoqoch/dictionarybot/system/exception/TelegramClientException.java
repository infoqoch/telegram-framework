package infoqoch.dictionarybot.system.exception;

import infoqoch.telegrambot.util.MarkdownStringBuilder;

public class TelegramClientException extends RuntimeException implements TelegramException{
    private final MarkdownStringBuilder response;

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

    public TelegramClientException(MarkdownStringBuilder response, String message, Throwable cause) {
        super(message, cause);
        this.response = response;
    }

    @Override
    public MarkdownStringBuilder response(){
        return response;
    }
}
