package infoqoch.dictionarybot.bot;

import infoqoch.dictionarybot.send.SendDispatcher;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.resolver.bean.SpringBeanContext;
import infoqoch.telegrambot.bot.DefaultTelegramBotFactory;
import infoqoch.telegrambot.bot.TelegramBot;
import org.reflections.util.ClasspathHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class DictionaryBotConfig {

    @Bean
    TelegramBot telegramBot(){
        return DefaultTelegramBotFactory.init("1959903402:AAFfvMCssvDcESLewCDvj5WZk83cbnIZ08o");
    }

    @Bean
    public UpdateDispatcher updateDispatcher(ApplicationContext context){
        final Collection<URL> urls = getUrlsExcludeTest();
        return new UpdateDispatcher(new SpringBeanContext(context), urls);
    }

    private Set<URL> getUrlsExcludeTest() {
        return ClasspathHelper.forPackage(UpdateDispatcher.class.getPackageName()).stream().filter(url -> !url.toString().contains("/test-classes")).collect(Collectors.toSet());
    }

    @Bean
    public SendDispatcher sendDispatcher(){
        return new SendDispatcher(telegramBot());
    }

}
