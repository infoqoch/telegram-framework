package infoqoch.dictionarybot.system.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter @Accessors(fluent = true)
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("dictionary")
public class DictionaryProperties {
    private final String sampleExcelPush;
    private final User user;
    private final Directory directory;

    @Getter @Accessors(fluent = true)
    @RequiredArgsConstructor
    public static class User {
        private final String promotionToAdmin;
    }


    @Getter @Accessors(fluent = true)
    @RequiredArgsConstructor
    public static class Directory {
        private final String excel;
    }
}

