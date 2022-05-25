package infoqoch.dictionarybot.request;

public class DictionaryRequestFactory {
    public static DictionaryRequest resolve(String input) {
        String message = input.replaceAll("_", " ");
        final int firstSpaceIdx = message.indexOf(" ");
        final DictionaryCommand command = extractCommand(message, firstSpaceIdx);
        String value = extractValue(message, firstSpaceIdx);
        return new DictionaryRequest(command, value);
    }

    private static DictionaryCommand extractCommand(String message, int firstSpaceIdx) {
        return DictionaryCommand.of(extractCommandStr(message, firstSpaceIdx));
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
