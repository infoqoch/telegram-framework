package infoqoch.dictionarybot.update.request;

public class UpdateRequestFactory {
    public static UpdateRequest resolve(String input) {
        String message = simpleString(input);
        final UpdateRequestCommand command = extractCommand(message);

        if(command==UpdateRequestCommand.UNKNOWN)
            return new UpdateRequest(command, message);

        return commandAndExtractedValue(message, command);
    }

    private static UpdateRequest commandAndExtractedValue(String message, UpdateRequestCommand command) {
        final int firstSpaceIdx = message.indexOf(command.alias());
        String value = extractValue(message, firstSpaceIdx+ command.alias().length());
        return new UpdateRequest(command, value);
    }

    private static String simpleString(String input) {
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
