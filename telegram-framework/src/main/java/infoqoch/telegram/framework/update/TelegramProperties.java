package infoqoch.telegram.framework.update;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

@Slf4j
@ToString
public class TelegramProperties {
    private static String PROPERTIES_FILE;
    private final String token;
    private final String fileUploadPath;
    private final Boolean sendMessageAfterUpdateResolved;

    public static TelegramProperties generate(String propertiesFileName) {
        PROPERTIES_FILE = propertiesFileName;

        return new TelegramProperties(
                findProperty("telegram.token")
                , findProperty("telegram.file-upload-path")
                , checkSendMessageAfterUpdateResolved()
        );
    }

    public static TelegramProperties generate() {
        PROPERTIES_FILE = "telegram-framework.properties";

        return new TelegramProperties(
                findProperty("telegram.token")
                , findProperty("telegram.file-upload-path")
                , checkSendMessageAfterUpdateResolved()
        );
    }

    private static Boolean checkSendMessageAfterUpdateResolved() {
        return Boolean.valueOf(findProperty("telegram.send-message-after-update-resolved"));
    }

    public String token() {
        if(token==null||token.length()==0)
            throw new IllegalArgumentException("telegram.token cannot be null");
        return token;
    }

    public String fileUploadPath() {
        return fileUploadPath;
    }

    private TelegramProperties(String token, String fileUploadPath, boolean sendMessageAfterUpdateResolved) {
        this.token = token;
        this.fileUploadPath = fileUploadPath;
        this.sendMessageAfterUpdateResolved = sendMessageAfterUpdateResolved;
    }

    private static String findProperty(String key) {
        try{
            try(InputStream stream = TelegramProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
                Properties p = new Properties();
                p.load(stream);
                return p.getProperty(key);
            }
        }catch (Exception e){
            throw new IllegalArgumentException("check properties ("+PROPERTIES_FILE+")", e);
        }
    }

    public boolean sendMessageAfterUpdateResolved() {
        return sendMessageAfterUpdateResolved;
    }
}