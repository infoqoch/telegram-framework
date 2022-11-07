package infoqoch.dictionarybot.system.properties;

import infoqoch.telegram.framework.update.util.TelegramProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DictionaryPropertiesTest {
    @Autowired
    TelegramProperties telegramProperties;

    @Autowired
    DictionaryProperties dictionaryProperties;

    @DisplayName("properties가 동작한다 : token")
    @Test
    void token(){
        final String token = telegramProperties.token();
        System.out.println("token = " + token);
        assertThat(token.length()).isGreaterThan(0);
    }

    @DisplayName("properties가 동작한다 : admin_code")
    @Test
    void promotion_to_admin_code(){
        final String promotionToAdmin = dictionaryProperties.user().promotionToAdmin();
        System.out.println("promotionToAdmin = " + promotionToAdmin);
        assertThat(promotionToAdmin.length()).isGreaterThan(0);
    }

}