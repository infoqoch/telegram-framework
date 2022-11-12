package infoqoch.telegram.framework.update.runner.bot;

import infoqoch.telegrambot.bot.TelegramUpdate;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;

import java.util.List;

public class FakeTelegramUpdate implements TelegramUpdate {
    public Response<List<Update>> mock;
    public boolean throwException = false;

    @Override
    public Response<List<Update>> get(long l) {
        if(throwException) throw new RuntimeException("예외다!!");
        return mock;
    }
}
