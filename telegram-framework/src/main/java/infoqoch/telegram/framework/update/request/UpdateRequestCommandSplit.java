package infoqoch.telegram.framework.update.request;

public class UpdateRequestCommandSplit {
    public static String flattingInput(String input) {
        return input.replaceAll("_", " ").replaceAll("/", "").replaceAll("[\\t\\s]+", " ").trim();
    }
}
