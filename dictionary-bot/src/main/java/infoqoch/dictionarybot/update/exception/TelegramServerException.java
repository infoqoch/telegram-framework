package infoqoch.dictionarybot.update.exception;

import infoqoch.telegrambot.util.MarkdownStringBuilder;

// TODO
// 필드가 반드시 MarkdownStringBuilder 어야 할까? 그냥 message가 더 낫지 않을까. 고민.
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
    public MarkdownStringBuilder response(){
        if(response==null)
            return new MarkdownStringBuilder().plain(getMessage());
        return response;
    }
}
