package infoqoch.telegram.framework.update.dispatcher.mapper.empty;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import infoqoch.telegram.framework.update.UpdateDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EmptyHandlerTest {

    private UpdateDispatcher updateDispatcher;

    @Test
    void empty(){
        assertThatThrownBy(
                ()-> new AnnotationConfigApplicationContext(InitConfig.class)
        )
                .getCause().isInstanceOf(BeanInstantiationException.class)
                .message().contains("*");
    }


    @EnableTelegramFramework
    @Configuration
    static class InitConfig {
        // no handler bean
    }
}
