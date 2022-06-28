package infoqoch.dictionarybot;

import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.request.UpdateWrapper;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import infoqoch.telegrambot.util.DefaultJsonBind;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

// @Component
//@ConditionalOnProperty(name = "is_test", havingValue = "N")
@AllArgsConstructor
public class ApplicationInfoPrint implements CommandLineRunner {

    private final UpdateDispatcher updateDispatcher;

    @Override
    public void run(String... args) throws Exception {
        String json = "{\"ok\":true,\"result\":[{\"update_id\":567841804,\n" +
                "\"message\":{\"message_id\":2102,\"from\":{\"id\":" + "12354" + ",\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652025791,\"text\":\"" + "help" + "\"}}]}";
        final Response<List<Update>> listResponse = new DefaultJsonBind().toList(json, Update.class);
        final Update update = listResponse.getResult().get(0);
        final UpdateResponse process = updateDispatcher.process(new UpdateWrapper(update));
        System.out.println("process.message() = " + process.message());
    }
}
