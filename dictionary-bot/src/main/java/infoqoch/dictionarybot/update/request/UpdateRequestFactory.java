package infoqoch.dictionarybot.update.request;

public class UpdateRequestFactory {
    public static UpdateRequest resolve(String input) {
        String message = input.replaceAll("_", " ");
        final int firstSpaceIdx = message.indexOf(" ");
        final UpdateRequestCommand command = extractCommand(message, firstSpaceIdx);
        String value = extractValue(message, firstSpaceIdx);
        return new UpdateRequest(command, value);
    }

    private static UpdateRequestCommand extractCommand(String message, int firstSpaceIdx) {
        return UpdateRequestCommand.of(extractCommandStr(message, firstSpaceIdx));
    }

    private static String extractValue(String message, int firstSpaceIdx) {
        if (firstSpaceIdx > -1)
            return message.substring(firstSpaceIdx).trim();
        return "";
    }

    private static String extractCommandStr(String message, int firstSpaceIdx) {
        if (firstSpaceIdx > -1)
            return message.substring(0, firstSpaceIdx).trim().replaceAll("/", "");
        return message.trim().replace("/", "");
    }
}
