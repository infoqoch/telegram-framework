package infoqoch.telegram.framework.update;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TelegramPropertiesTest {
    @Test
    void token_and_file_path(){
        final TelegramProperties properties = TelegramProperties.generate("telegram-framework.properties");
        assertThat(properties.token()).isNotEmpty();
        assertThat(properties.token()).contains(":");
        assertThat(properties.fileUploadPath()).isNotEmpty();
        assertThat(properties.sendMessageAfterUpdateResolved()).isNotNull();
        System.out.println("properties = " + properties);
    }
}