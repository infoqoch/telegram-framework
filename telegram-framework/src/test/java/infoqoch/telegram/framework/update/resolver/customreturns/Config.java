package infoqoch.telegram.framework.update.resolver.customreturns;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import infoqoch.telegram.framework.update.resolver.custom.CustomUpdateRequestReturnRegister;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableTelegramFramework
public class Config {
    @Bean
    public AnyHandler sampleHandler() {
        return new AnyHandler();
    }

    @Bean
    public CustomUpdateRequestReturnRegister customUpdateRequestReturnRegister() {
        return new SampleCustomUpdateRequestReturnRegister();
    }
}
