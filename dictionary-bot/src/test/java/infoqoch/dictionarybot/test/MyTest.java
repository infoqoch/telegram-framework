package infoqoch.dictionarybot.test;

import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.bot.TelegramUpdate;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.bot.response.SendMessageResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MyTest {
    @Autowired
    TelegramBot telegramBot;

    @Test
    @Disabled
    void test(){
        final TelegramUpdate update = telegramBot.update();
        final Response<List<Update>> listResponse = update.get(0l);
        System.out.println("listResponse = " + listResponse);
    }

    @Test
    @Disabled
    void test2(){
        final TelegramSend send = telegramBot.send();
        final Response<SendMessageResponse> message = send.message(new SendMessageRequest(39327045l, "hi!"));
        System.out.println("message = " + message);
    }
}
