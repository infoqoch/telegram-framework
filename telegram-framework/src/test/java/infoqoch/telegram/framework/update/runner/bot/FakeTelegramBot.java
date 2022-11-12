package infoqoch.telegram.framework.update.runner.bot;

import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.TelegramFile;
import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.bot.TelegramUpdate;
import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.config.TelegramUrls;

public class FakeTelegramBot implements TelegramBot {
    private final TelegramSend telegramSend;
    private final TelegramUpdate telegramUpdate;

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
    public TelegramUrls url() {
        return TelegramBotProperties.defaultProperties("token").url();
    }
}
