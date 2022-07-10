package infoqoch.dictionarybot;

import infoqoch.dictionarybot.send.repository.SendRepository;
import infoqoch.dictionarybot.system.property.TelegramProperties;
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
    private final SendRepository sendRepository;

    @Bean
    TelegramBot telegramBot(){
        return DefaultTelegramBotFactory.init(telegramProperties.getToken());
    }

    @Bean
    DictionarySendRunner dictionarySendRunner(){
        return new DictionarySendRunner(telegramBot().send(), sendRepository);
    }
}
