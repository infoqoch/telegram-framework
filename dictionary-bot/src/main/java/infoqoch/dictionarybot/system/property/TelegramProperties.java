package infoqoch.dictionarybot.system.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "telegram")
public class TelegramProperties {
    private String token;

    public String getToken() {
        if(token==null||token.length()==0)
            throw new IllegalArgumentException("telegram.token cannot be null");
        return token;
    }
}
