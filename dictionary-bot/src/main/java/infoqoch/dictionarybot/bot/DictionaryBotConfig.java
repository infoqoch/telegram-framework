package infoqoch.dictionarybot.bot;

import infoqoch.telegrambot.bot.DefaultTelegramBot;
import infoqoch.telegrambot.bot.DefaultTelegramBotFactory;
import infoqoch.telegrambot.bot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DictionaryBotConfig {

    @Bean
    TelegramBot telegramBot(){
        return DefaultTelegramBotFactory.init("1959903402:AAFfvMCssvDcESLewCDvj5WZk83cbnIZ08o");
    }

}
