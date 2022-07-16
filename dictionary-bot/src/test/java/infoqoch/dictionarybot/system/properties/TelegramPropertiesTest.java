package infoqoch.dictionarybot.system.properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TelegramPropertiesTest {
    @Autowired
    TelegramProperties telegramProperties;

    @Test
    void token(){
        final String token = telegramProperties.token();
        System.out.println("token = " + token);
        assertThat(token.length()).isGreaterThan(0);
    }

    @Test
    void promotion_to_admin_code(){
        final String promotionToAdmin = telegramProperties.user().promotionToAdmin();
        System.out.println("promotionToAdmin = " + promotionToAdmin);
        assertThat(promotionToAdmin.length()).isGreaterThan(0);
    }

}