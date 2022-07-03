package infoqoch.dictionarybot.update.request;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UpdateRequestMessage {
    private final UpdateRequestCommand command;
    private final String value;

    public UpdateRequestMessage(UpdateRequestCommand command, String value) {
        this.command = command;
        this.value = value;
    }

    public UpdateRequestCommand command() {
        return getCommand();
    }
    public String value() {
        return getValue();
    }
}
