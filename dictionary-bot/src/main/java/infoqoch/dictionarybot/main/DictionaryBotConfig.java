package infoqoch.dictionarybot.main;

import infoqoch.dictionarybot.send.service.SendRunnerService;
import infoqoch.dictionarybot.system.properties.TelegramProperties;
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
    private final SendRunnerService sendRunnerService;

    @Bean
    TelegramBot telegramBot(){
        return DefaultTelegramBotFactory.init(telegramProperties.token());
    }

    @Bean
    DictionarySendRunner dictionarySendRunner(){
        return new DictionarySendRunner(telegramBot().send(), sendRunnerService);
    }
}
