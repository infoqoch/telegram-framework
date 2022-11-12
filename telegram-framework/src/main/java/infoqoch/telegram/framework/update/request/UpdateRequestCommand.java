package infoqoch.telegram.framework.update.request;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class UpdateRequestCommand {
    private final String COMMAND;

    private UpdateRequestCommand(String command) {
        this.COMMAND = command;
    }

    public String get(){
        return COMMAND;
    }

    public static UpdateRequestCommand of(String commandStr) {
        if(commandStr==null || commandStr.isBlank()) throw new IllegalArgumentException("command should not be blank");
        return new UpdateRequestCommand(flatting(commandStr));
    }

    public boolean startsWith(String input) {
        final String flatInput = flatting(input);
        if(flatInput.equals(COMMAND)) return true;
        return flatInput.startsWith(commandWithSpace());
    }

    @Override
    public String toString() {
        return "UpdateRequestCommand(" + COMMAND +")";
    }

    public String extractValue(String input) {
        final String flatInput = flatting(input);
        final int length = COMMAND.equals("*")?0:COMMAND.length();
        return flatting(flatInput.substring(length));
    }

    private String commandWithSpace(){
        return COMMAND + " ";
    }

    private static String flatting(String commandStr) {
        return UpdateRequestCommandSplit.flattingInput(commandStr);
    }

}
