package infoqoch.mock.bot;

import infoqoch.telegrambot.bot.TelegramUpdate;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import lombok.Data;

import java.util.List;

@Data
public class FakeTelegramUpdate implements TelegramUpdate {
    private Response<List<Update>> mock;

    private boolean throwException;

    @Override
    public Response<List<Update>> get(long l) {
        if(throwException) throw new RuntimeException("예외다!!");
        return mock;
    }
}
