package infoqoch.dictionarybot.main;

import infoqoch.dictionarybot.update.util.TelegramProperties;
import infoqoch.telegrambot.bot.DefaultTelegramBotFactory;
import infoqoch.telegrambot.bot.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DictionaryBotConfig {
    private final TelegramProperties telegramProperties;

    @Bean
    TelegramBot telegramBot(){
        return DefaultTelegramBotFactory.init(telegramProperties.token());
    }
}
