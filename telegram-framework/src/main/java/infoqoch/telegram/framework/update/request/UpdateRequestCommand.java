package infoqoch.telegram.framework.update.request;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class UpdateRequestCommand {
    private final String COMMAND;
    private final String COMMAND_WITH_SPACE;

    private UpdateRequestCommand(String command) {
        this.COMMAND = command;
        this.COMMAND_WITH_SPACE = COMMAND + " ";
    }

    public String get(){
        return COMMAND;
    }

    public static UpdateRequestCommand of(String commandStr) {
        if(commandStr==null || commandStr.isBlank()) throw new IllegalArgumentException("command should not be blank");
        return new UpdateRequestCommand(flatting(commandStr));
    }

    private static boolean isMatch(String input, String alias) {
        String[] inputSplit = input.split(" ");
        String[] aliasSplit = alias.split(" ");

        if(inputSplit.length < aliasSplit.length) return false;

        for(int i = 0; i< aliasSplit.length; i++)
            if(!aliasSplit[i].equals(inputSplit[i])) return false;

        return true;
    }

    public boolean startsWith(String input) {
        final String flatInput = flatting(input);
        if(flatInput.equals(COMMAND)) return true;
        return flatInput.startsWith(COMMAND_WITH_SPACE);
    }

    @Override
    public String toString() {
        return "UpdateRequestCommand(" + COMMAND +")";
    }

    public String extractValue(String input) {
        final String flatInput = flatting(input);
        System.out.println("flatInput = " + flatInput);
        final int length = COMMAND.equals("*")?0:COMMAND.length();
        return flatting(flatInput.substring(length));
    }

    private static String flatting(String commandStr) {
        return UpdateRequestCommandSplit.flattingInput(commandStr);
    }


}
