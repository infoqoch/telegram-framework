package infoqoch.dictionarybot.bot;

import infoqoch.dictionarybot.send.SendDispatcher;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.resolver.bean.SpringBeanContext;
import infoqoch.telegrambot.bot.DefaultTelegramBotFactory;
import infoqoch.telegrambot.bot.TelegramBot;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DictionaryBotConfig {

    @Bean
    TelegramBot telegramBot(){
        return DefaultTelegramBotFactory.init("1959903402:AAFfvMCssvDcESLewCDvj5WZk83cbnIZ08o");
    }

    @Bean
    public UpdateDispatcher updateDispatcher(ApplicationContext context){
        final String packagePath = UpdateDispatcher.class.getPackageName() + ".controller";
        // final String packagePath = this.getClass().getPackageName();
        return new UpdateDispatcher(packagePath, new SpringBeanContext(context));
    }

    @Bean
    public SendDispatcher sendDispatcher(){
        return new SendDispatcher(telegramBot());
    }

}
