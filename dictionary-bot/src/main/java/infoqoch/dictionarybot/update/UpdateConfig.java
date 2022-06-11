package infoqoch.dictionarybot.update;

import infoqoch.dictionarybot.update.resolver.util.SpringBeanContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateConfig {
    @Bean
    public UpdateDispatcher updateDispatcher(ApplicationContext context){
        final String packagePath = this.getClass().getPackageName() + ".controller";
        // final String packagePath = this.getClass().getPackageName();
        return new UpdateDispatcher(packagePath, new SpringBeanContext(context));
    }
}
