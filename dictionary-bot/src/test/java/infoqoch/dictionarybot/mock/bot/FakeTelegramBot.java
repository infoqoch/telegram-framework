package infoqoch.dictionarybot.mock.bot;

import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.TelegramFile;
import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.bot.TelegramUpdate;
import infoqoch.telegrambot.bot.config.TelegramBotProperties;

public class FakeTelegramBot implements TelegramBot {
    private final TelegramSend telegramSend;
    private final TelegramUpdate telegramUpdate;

    public FakeTelegramBot(TelegramSend telegramSend) {
        this.telegramSend = telegramSend;
        this.telegramUpdate = null;
    }

    public FakeTelegramBot(TelegramUpdate telegramUpdate, TelegramSend telegramSend) {
        this.telegramSend = telegramSend;
        this.telegramUpdate = telegramUpdate;
    }


    @Override
    public TelegramSend send() {
        return telegramSend;
    }

    @Override
    public TelegramUpdate update() {
        return telegramUpdate;
    }

    @Override
    public TelegramFile file() {
        return null;
    }

    @Override
    public TelegramBotProperties.Url url() {
        return null;
    }
}
