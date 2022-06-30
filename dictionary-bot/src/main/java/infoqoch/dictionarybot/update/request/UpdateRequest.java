package infoqoch.dictionarybot.update.request;

import infoqoch.dictionarybot.system.exception.TelegramServerException;
import infoqoch.dictionarybot.update.request.body.UpdateChat;
import infoqoch.dictionarybot.update.request.body.UpdateDocument;
import infoqoch.dictionarybot.update.request.body.UpdateType;
import infoqoch.telegrambot.bot.entity.Update;

import java.util.Optional;

import static infoqoch.dictionarybot.update.request.body.UpdateType.*;

public class UpdateRequest {
    private final Update update;

    public UpdateRequest(Update update) {
        this.update = update;
    }

    public Long chatId(){
        return toChat().getChat().getId();
    }
    public UpdateType type() {
        if(update.getEditedMessage()!=null) return EDITED;
        if(update.getMessage().getPhoto()!=null) return PHOTO;
        if(update.getMessage().getDocument()!=null) return DOCUMENT;
        if(update.getMessage().getText()!=null) return CHAT;
        throw new TelegramServerException("unknown update type (1)");
    }

    public UpdateRequestCommand command(){
        return updateRequestMessage().command();
    }

    public String value(){
        return updateRequestMessage().value();
    }

    public UpdateRequestMessage updateRequestMessage() {
        if(type() == CHAT) return UpdateRequestFactory.resolve(update.getMessage().getText());
        if(type() == DOCUMENT) return UpdateRequestFactory.resolve(update.getMessage().getCaption());
        if(type() == PHOTO) return UpdateRequestFactory.resolve(update.getMessage().getCaption());
        throw new TelegramServerException("unknown update type (2)");
    }

    public UpdateChat toChat() {
        return UpdateChat.builder()
                .updateId(update.getUpdateId())
                .messageId(update.getMessage().getMessageId())
                .date(update.getMessage().getDate())
                .text(update.getMessage().getText())
                .from(update.getMessage().getFrom())
                .chat(update.getMessage().getChat())
                .build();
    }

    public UpdateDocument toDocument() {
        return UpdateDocument.builder()
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

    public Optional<Object> getBodyByType(Class<?> type) {
        if(type == UpdateChat.class) return Optional.of(toChat());
        if(type == UpdateDocument.class) return Optional.of(toDocument());
        throw new TelegramServerException("not support data type (3)");
    }
}
