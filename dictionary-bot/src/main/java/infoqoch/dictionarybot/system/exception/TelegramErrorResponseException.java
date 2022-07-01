package infoqoch.dictionarybot.system.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TelegramErrorResponseException extends RuntimeException {
    private final String errorCode;
    private final String description;
}
