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

    public ChatRequest toChat() {
        return ChatRequest.builder()
                .updateId(update.getUpdateId())
                .messageId(update.getMessage().getMessageId())
                .date(update.getMessage().getDate())
                .text(update.getMessage().getText())
                .from(update.getMessage().getFrom())
                .chat(update.getMessage().getChat())
                .build();
    }

    public DocumentRequest toDocument() {
        return DocumentRequest.builder()
                .updateId(update.getUpdateId())
                .messageId(update.getMessage().getMessageId())
                .date(update.getMessage().getDate())
                .caption(update.getMessage().getCaption())
                .from(update.getMessage().getFrom())
                .chat(update.getMessage().getChat())
                .document(update.getMessage().getDocument())
                .build();
    }

    public Object toPhoto() throws Exception {
        throw new Exception(new UnsupportedOperationException("not support operation"));
    }
}
