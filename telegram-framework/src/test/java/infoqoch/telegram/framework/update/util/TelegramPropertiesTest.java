package infoqoch.telegram.framework.update.util;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class TelegramPropertiesTest {
    @Test
    void test(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(Config.class);
        final TelegramProperties bean = ac.getBean(TelegramProperties.class);
        assertThat(bean.token()).isNotEmpty();
        assertThat(bean.fileUploadPath()).isNotEmpty();
    }

    @Configuration
    static class Config{
        @Bean
        TelegramProperties telegramProperties(){
            return TelegramProperties.generate();
        }

    }

}