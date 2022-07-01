package infoqoch.dictionarybot.send;

import infoqoch.telegrambot.bot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SendDispatcherConfig {
    private final TelegramBot telegramBot;

    @Bean
    public SendDispatcher sendDispatcher(){
        return new SendDispatcher(telegramBot);
    }
}
