package infoqoch.telegram.framework.update;

import infoqoch.telegram.framework.update.TelegramProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TelegramPropertiesTest {
    @Test
    void token_and_file_path(){
        final TelegramProperties bean = TelegramProperties.generate();
        assertThat(bean.token()).isNotEmpty();
        assertThat(bean.token()).contains(":");
        assertThat(bean.fileUploadPath()).isNotEmpty();
    }
}