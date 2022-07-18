package infoqoch.dictionarybot.update.request;

public enum UpdateRequestCommand {
    HELP("help")
    , LOOKUP_WORD("w", "ㄷ"),  LOOKUP_SENTENCE("s", "ㅁ"), LOOKUP_DEFINITION("d", "ㅈ"), LOOKUP_FULL_SEARCH("f", "ㅍ")
    ,  UNKNOWN("unknown")
    , EXCEL_PUSH("excel push", "push", "replace")
    , HOURLY_ALARM("hourly")
    , SHARE_MINE("share mine"), LOOKUP_ALL_USERS("lookup all users")
    , PROMOTION_ROLE("promotion")
    , MY_STATUS("status", "상태");

    private final String value;
    private final String[] aliases;

    UpdateRequestCommand(String value, String... aliases) {
        this.value = value;
        this.aliases = aliases != null ? aliases : new String[]{};
    }

    public String value(){
        return value;
    }

    private String[] alias(){
        return aliases;
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

            if(isMatch(input, command.value)) return command;

            for (String alias : command.aliases)
                if (isMatch(input, alias)) return command;
        }

        return UNKNOWN;
    }

    private static boolean isMatch(String input, String alias) {
        String[] inputSplit = input.split(" ");
        String[] aliasSplit = alias.split(" ");

        if(inputSplit.length < aliasSplit.length) return false;

        for(int i = 0; i< aliasSplit.length; i++)
            if(!aliasSplit[i].equals(inputSplit[i])) return false;

        return true;
    }
}
