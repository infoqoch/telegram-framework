package infoqoch.dictionarybot.api;

import infoqoch.telegrambot.bot.DefaultTelegramBotFactory;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.entity.Message;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TelegramBotIntegrationTest {
    private TelegramBot bot;

    @BeforeEach
    private void setUp() {
        bot =  DefaultTelegramBotFactory.init("1959903402:AAFfvMCssvDcESLewCDvj5WZk83cbnIZ08o");
    }

    // @Test
    @DisplayName("TelegramBot.send() 테스트")
    void send_message(){
        // when
        final Response<Message> response = bot.send().message(new SendMessageRequest(39327045, "hi, 반가반가"));

        // then
        assertThat(response.isOk()).isTrue();
    }
}
