package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.send.request.SendRequest;
import infoqoch.dictionarybot.send.response.SendResponse;
import infoqoch.dictionarybot.send.exception.TelegramErrorResponseException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static infoqoch.dictionarybot.send.Send.Status.*;

@ToString
@Getter
public class Send {
    private Long no;
    private SendRequest request;
    private Status status;

    @Builder
    public Send(Long no, SendRequest request, Status status) {
        this.no = no;
        this.request = request;
        this.status = status;
    }

    private String errorCode;
    private String errorMessage;

    public enum Status{
        REQUEST, SENDING, SUCCESS, RESPONSE_ERROR, ERROR;
    }

    public static Send of(SendRequest request) {
        return Send.builder()
                .request(request)
                .status(REQUEST)
                .build();
    }

    public void sending(){
        this.status = SENDING;
    }

    public void success(SendResponse sendResponse) {
        this.status = SUCCESS;
    }

    public void responseError(TelegramErrorResponseException e) {
        this.status = RESPONSE_ERROR;
        this.errorCode = e.getErrorCode();
        this.errorMessage = e.getDescription();
    }

    public void error(Exception e) {
        this.status = ERROR;
        this.errorMessage = e.getMessage();
    }
}