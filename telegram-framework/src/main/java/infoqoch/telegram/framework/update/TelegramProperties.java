package infoqoch.telegram.framework.update;

import lombok.ToString;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Properties;

@ToString
public class TelegramProperties {
    private static final String PROPERTIES_FILE = "telegram-framework.properties";
    private final String token;
    private final String fileUploadPath;

    public static TelegramProperties generate() {
        final String token = findProperty("telegram.token");
        final String fileUploadPath = findProperty("telegram.file-upload-path");
        return new TelegramProperties(token, fileUploadPath);
    }

    public String token() {
        if(token==null||token.length()==0)
            throw new IllegalArgumentException("telegram.token cannot be null");
        return token;
    }

    public String fileUploadPath() {
        return fileUploadPath;
    }

    private TelegramProperties(String token, String fileUploadPath) {
        this.token = token;
        this.fileUploadPath = fileUploadPath;
    }

    private static String findProperty(String key) {
        try{
            final URL resource = TelegramProperties.class.getClassLoader().getResource(PROPERTIES_FILE);
            File path = new File(resource.toURI());
            try(FileReader file = new FileReader(path)) {
                Properties p = new Properties();
                p.load(file);
                return p.getProperty(key);
            }
        }catch (Exception e){
            throw new IllegalArgumentException("check properties ("+PROPERTIES_FILE+")", e);
        }
    }

    public boolean sendMessageAfterUpdateResolved() {
        return true;
    }
}