package infoqoch.dictionarybot.system.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({DictionaryProperties.class})
public class ConfigProperties {
}
