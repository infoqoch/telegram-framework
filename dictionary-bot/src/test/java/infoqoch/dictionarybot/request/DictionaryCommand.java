package infoqoch.dictionarybot.request;

public enum DictionaryCommand {
    HELP("help"), UNKNOWN("unknown");

    private final String alias;

    DictionaryCommand(String alias) {
        this.alias = alias;
    }

    public static DictionaryCommand of(String input) {
        for(DictionaryCommand command : DictionaryCommand.values()){
            if(command.alias.equals(input))
                return command;
        }
        return UNKNOWN;
    }
}
