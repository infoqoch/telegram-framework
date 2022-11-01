package infoqoch.dictionarybot.update.util;

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

    public String token() {
        if(token==null||token.length()==0)
            throw new IllegalArgumentException("telegram.token cannot be null");
        return token;
    }
}

