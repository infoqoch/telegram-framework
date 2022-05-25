package infoqoch.dictionarybot.request.update;

import infoqoch.telegrambot.bot.entity.Update;

import static infoqoch.dictionarybot.request.update.UpdateType.*;

public class UpdateWrapper {
    private final Update update;

    public UpdateWrapper(Update update) {
        this.update = update;
    }

    public UpdateType type() {
        if(update.getEditedMessage()!=null) return EDITED;
        if(update.getMessage().getPhoto()!=null) return PHOTO;
        if(update.getMessage().getDocument()!=null) return DOCUMENT;
        if(update.getMessage().getText()!=null) return CHAT;
        throw new IllegalStateException("unknown type");
    }
}
