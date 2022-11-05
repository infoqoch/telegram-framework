package infoqoch.telegram.framework.update.dispatcher.mapper.star;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import infoqoch.telegram.framework.update.UpdateDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NoStarHandlerTest {

    private UpdateDispatcher updateDispatcher;

    @Test
    void no_star(){
        assertThatThrownBy(
                ()-> new AnnotationConfigApplicationContext(InitConfig.class)
        )
                .getCause().isInstanceOf(BeanInstantiationException.class)
                .message().contains("*");
    }


    @EnableTelegramFramework
    @Configuration
    static class InitConfig {
        @Bean
        NoStarSampleHandler sampleHandler(){
            return new NoStarSampleHandler();
        }
    }
}
