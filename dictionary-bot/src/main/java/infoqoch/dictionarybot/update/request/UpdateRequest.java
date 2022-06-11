package infoqoch.dictionarybot.update.request;

import lombok.Getter;

@Getter
public class UpdateRequest {
    private final UpdateRequestCommand command;
    private final String value;

    public UpdateRequest(UpdateRequestCommand command, String value) {
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
