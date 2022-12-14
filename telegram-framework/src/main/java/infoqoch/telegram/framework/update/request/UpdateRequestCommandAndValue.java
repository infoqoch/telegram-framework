package infoqoch.telegram.framework.update.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpdateRequestCommandAndValue {
    private final UpdateRequestCommand command;
    private final String value;

    public UpdateRequestCommandAndValue(UpdateRequestCommand command, String value) {
        this.command = command;
        this.value = value;
    }
}
