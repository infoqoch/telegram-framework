package infoqoch.dictionarybot.log.send;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SendResult {
    private final Send.Status status;
    private final int errorCode;
    private final String errorMessage;
    private final SendRequest request;
}
