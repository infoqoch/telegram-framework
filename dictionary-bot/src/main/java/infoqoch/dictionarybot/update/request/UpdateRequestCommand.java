package infoqoch.dictionarybot.update.request;

public enum UpdateRequestCommand {
    HELP("help"), LOOKUP_WORD("w"), UNKNOWN("unknown");

    private final String alias;

    UpdateRequestCommand(String alias) {
        this.alias = alias;
    }

    public static UpdateRequestCommand of(String input) {
        for(UpdateRequestCommand command : UpdateRequestCommand.values()){
            if(command.alias.equals(input))
                return command;
        }
        return UNKNOWN;
    }
}
