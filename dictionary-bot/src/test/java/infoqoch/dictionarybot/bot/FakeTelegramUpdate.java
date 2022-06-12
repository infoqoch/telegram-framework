package infoqoch.dictionarybot.bot;

import infoqoch.telegrambot.bot.TelegramUpdate;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import lombok.Data;

import java.util.List;

@Data
public class FakeTelegramUpdate implements TelegramUpdate {
    private Response<List<Update>> mock;

    @Override
    public Response<List<Update>> get(long l) {
        return mock;
    }
}
