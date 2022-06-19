package infoqoch.dictionarybot.update.request;

public enum UpdateRequestCommand {
    HELP("help"), LOOKUP_WORD("w"),  LOOKUP_SENTENCE("s"), LOOKUP_DEFINITION("d"),  UNKNOWN("unknown"), EXCEL_PUSH("excel push");

    private final String alias;

    UpdateRequestCommand(String alias) {
        this.alias = alias;
    }

    public String alias(){
        return alias;
    }

    // 만약 여러 개의 alias가 있을 경우, contain을 통해 추출한 명령어를 얻어야 할 수 있음.
//    public static Optional<String> contains(String input){
//        for(UpdateRequestCommand command : UpdateRequestCommand.values()){
//            if(command.alias.contains(input)){
//                return Optional.of(command.alias());
//            }
//        }
//        return Optional.empty();
//    }

    public static UpdateRequestCommand of(String input) {
        for(UpdateRequestCommand command : UpdateRequestCommand.values()){
            final String[] aliases = command.alias.split(" ");

            if (isNotMatched(input.split(" "), aliases)) continue;

            return command;
        }
        return UNKNOWN;
    }

    private static boolean isNotMatched(String[] inputs, String[] aliases) {
        if(inputs.length < aliases.length) return true;

        for(int i = 0; i< aliases.length; i++)
            if(!aliases[i].equals(inputs[i])) return true;

        return false;
    }
}
