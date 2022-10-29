package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.send.Send.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SendResult {
    private final Status status;
    private final int errorCode;
    private final String errorMessage;
    private final SendRequest request;
}
