package infoqoch.telegram.framework.update.dispatcher.empty;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import infoqoch.telegram.framework.update.UpdateConfig;
import infoqoch.telegram.framework.update.UpdateDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

public class EmptyHandlerTest {

    private UpdateDispatcher updateDispatcher;

    @BeforeEach
    void setUp(){

    }

    @Test
    void test(){
        final ApplicationContext ac = new AnnotationConfigApplicationContext(InitConfig.class);

        System.out.println("hihihi!!@");
        final Map<String, Object> beansWithAnnotation = ac.getBeansWithAnnotation(EnableTelegramFramework.class);
        System.out.println("beansWithAnnotation.size() = " + beansWithAnnotation.size());
        

        final UpdateConfig updateConfig = new UpdateConfig();
        updateDispatcher = updateConfig.updateDispatcher(ac);
    }


    @EnableTelegramFramework
    @Configuration
    static class InitConfig {
        // no handler bean
    }
}
