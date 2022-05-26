package infoqoch.dictionarybot.request.update;

import infoqoch.dictionarybot.request.DictionaryRequest;
import infoqoch.dictionarybot.request.DictionaryRequestFactory;
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
        throw new IllegalStateException("unknown update type");
    }

    public DictionaryRequest command() {
        if(type() == CHAT) return DictionaryRequestFactory.resolve(update.getMessage().getText());
        if(type() == DOCUMENT) return DictionaryRequestFactory.resolve(update.getMessage().getCaption());
        if(type() == PHOTO) return DictionaryRequestFactory.resolve(update.getMessage().getCaption());
        throw new IllegalStateException("unknown update type (2)");
    }
}
