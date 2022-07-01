package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.send.request.SendRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static infoqoch.dictionarybot.send.Send.Status.*;

@Getter
@AllArgsConstructor @Builder
public class Send {
    private Long no;
    private SendRequest request;
    private Status status;

    public enum Status{
        REQUEST, SENDING, SUCCESS, RESPONSE_ERROR, ERROR;
    }

    public static Send of(SendRequest request) {
        return Send.builder()
                .request(request)
                .status(REQUEST)
                .build();
    }
}
