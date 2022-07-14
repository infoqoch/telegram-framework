package infoqoch.dictionarybot;

import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import infoqoch.telegrambot.util.DefaultJsonBind;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class
DictionaryBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(DictionaryBotApplication.class, args);
    }

    @RequiredArgsConstructor
    public static class Printer implements CommandLineRunner {

        private final UpdateDispatcher updateDispatcher;

        @Override
        public void run(String... args) throws Exception {
            String json = "{\"ok\":true,\"result\":[{\"update_id\":567841804,\n" +
                    "\"message\":{\"message_id\":2102,\"from\":{\"id\":" + "12354" + ",\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652025791,\"text\":\"" + "help" + "\"}}]}";
            final Response<List<Update>> listResponse = new DefaultJsonBind().toList(json, Update.class);
            final Update update = listResponse.getResult().get(0);
            final UpdateResponse process = updateDispatcher.process(new UpdateRequest(update));
            System.out.println("process.message() = " + process.message());
        }
    }
}
