package infoqoch.dictionarybot.system.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "telegram")
public class TelegramProperties {
    private String token;
}
