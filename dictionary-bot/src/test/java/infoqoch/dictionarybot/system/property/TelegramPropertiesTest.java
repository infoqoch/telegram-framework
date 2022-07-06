package infoqoch.dictionarybot.system.property;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TelegramPropertiesTest {
    @Autowired
    TelegramProperties telegramProperties;

    @Test
    void test(){
        final String token = telegramProperties.getToken();
        System.out.println("token = " + token);
    }

}