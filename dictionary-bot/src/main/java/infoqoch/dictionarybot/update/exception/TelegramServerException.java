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
        // if(response==null) return new MarkdownStringBuilder().plain(getMessage()); // message는 서버 내부의 로깅을 위한 내용임. 명시적으로 사용자에게 전달하기 위한 메시지는 아님.
        return response; // 명시적으로 보내기 위한 데이터 타입이 MSB임.
    }
}
