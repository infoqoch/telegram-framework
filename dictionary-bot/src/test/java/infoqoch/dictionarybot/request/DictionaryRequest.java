package infoqoch.dictionarybot.request;

import lombok.Getter;

@Getter
public class DictionaryRequest {
    private final DictionaryCommand command;
    private final String value;

    public DictionaryRequest(DictionaryCommand command, String value) {
        this.command = command;
        this.value = value;
    }

    public DictionaryCommand command() {
        return getCommand();
    }

    public String value() {
        return getValue();
    }
}
