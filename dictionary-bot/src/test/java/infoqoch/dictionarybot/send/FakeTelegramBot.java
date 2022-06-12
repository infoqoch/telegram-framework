package infoqoch.dictionarybot.send;

import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.TelegramFile;
import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.bot.TelegramUpdate;

public class FakeTelegramBot implements TelegramBot {
    private final TelegramSend telegramSend;

    public FakeTelegramBot(TelegramSend telegramSend) {
        this.telegramSend = telegramSend;
    }


    @Override
    public TelegramSend send() {
        return telegramSend;
    }

    @Override
    public TelegramUpdate update() {
        return null;
    }

    @Override
    public TelegramFile file() {
        return null;
    }
}
