package infoqoch.dictionarybot.system.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter @Accessors(fluent = true)
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("telegram")
public class TelegramProperties {
    private final String token;
    private final String sampleExcelPush;
    private final User user;


    @Getter @Accessors(fluent = true)
    @RequiredArgsConstructor
    public static class User {
        private final String promotionToAdmin;
    }

    public String token() {
        if(token==null||token.length()==0)
            throw new IllegalArgumentException("telegram.token cannot be null");
        return token;
    }

}
