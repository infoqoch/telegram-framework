package infoqoch.dictionarybot.update.request;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UpdateRequestCommandAndValue {
    private final UpdateRequestCommand command;
    private final String value;

    public UpdateRequestCommandAndValue(UpdateRequestCommand command, String value) {
        this.command = command;
        this.value = value;
    }
}
