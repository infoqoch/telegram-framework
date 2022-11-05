package infoqoch.telegram.framework.update.resolver.customparam;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import infoqoch.telegram.framework.update.resolver.custom.CustomUpdateRequestParamRegister;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableTelegramFramework
public class Config {
    @Bean
    public SampleHandler sampleHandler(){
        return new SampleHandler();
    }

    @Bean
    public CustomUpdateRequestParamRegister customUpdateRequestParamRegister(){
        return new SampleCustomUpdateRequestParamRegister();
    }
}
