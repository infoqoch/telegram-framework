package infoqoch.dictionarybot.update.exception;

import infoqoch.telegrambot.util.MarkdownStringBuilder;

public interface TelegramException {
    MarkdownStringBuilder response();
}
