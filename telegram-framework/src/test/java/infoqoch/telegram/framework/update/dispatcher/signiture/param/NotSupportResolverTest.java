package infoqoch.telegram.framework.update.dispatcher.signiture.param;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NotSupportResolverTest {
    @Test
    void not_support_param(){
        assertThatThrownBy(()->
                new AnnotationConfigApplicationContext(InitConfig.class)
                ).isInstanceOf(BeanCreationException.class)
                .getCause().getCause().isInstanceOf(IllegalArgumentException.class)
                .message().contains("can not resolve the parameter type");

    }


    @EnableTelegramFramework
    @Configuration
    static class InitConfig {
        @Bean
        ParamHandler sampleHandler(){
            return new ParamHandler();
        }
    }
}
