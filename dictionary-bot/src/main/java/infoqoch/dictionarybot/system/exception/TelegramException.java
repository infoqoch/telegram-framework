package infoqoch.dictionarybot.system.exception;

import infoqoch.telegrambot.util.MarkdownStringBuilder;

public interface TelegramException {
    MarkdownStringBuilder response();
}
