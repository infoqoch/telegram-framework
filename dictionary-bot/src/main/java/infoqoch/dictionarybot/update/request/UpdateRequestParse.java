package infoqoch.dictionarybot.update.request;

public class UpdateRequestParse {
    public static UpdateRequestMessage resolve(String input) {
        String message = flattingInput(input);
        final UpdateRequestCommand command = extractCommand(message);

        if(command==UpdateRequestCommand.UNKNOWN)
            return new UpdateRequestMessage(command, message);

        return extractValueWithCommand(message, command);
    }

    private static UpdateRequestMessage extractValueWithCommand(String message, UpdateRequestCommand command) {
        final int firstSpaceIdx = message.indexOf(command.alias());
        String value = extractValue(message, firstSpaceIdx+ command.alias().length());
        return new UpdateRequestMessage(command, value);
    }

    private static String flattingInput(String input) {
        return input.replaceAll("_", " ").replaceAll("/", "").replaceAll("[\\t\\s]+", " ").trim();
    }

    private static UpdateRequestCommand extractCommand(String message) {
        return UpdateRequestCommand.of(message);
    }

    private static String extractValue(String message, int firstSpaceIdx) {
        if (firstSpaceIdx > -1)
            return message.substring(firstSpaceIdx).trim();
        return "";
    }
}
