package infoqoch.dictionarybot.update.request.body;

import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.request.UpdateRequestCommand;
import infoqoch.dictionarybot.update.request.UpdateRequestFactory;
import infoqoch.telegrambot.bot.entity.Update;

import java.util.Optional;

import static infoqoch.dictionarybot.update.request.body.UpdateType.*;

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

    public UpdateRequestCommand command(){
        return commandAndValue().command();
    }

    public String value(){
        return commandAndValue().value();
    }

    public UpdateRequest commandAndValue() {
        if(type() == CHAT) return UpdateRequestFactory.resolve(update.getMessage().getText());
        if(type() == DOCUMENT) return UpdateRequestFactory.resolve(update.getMessage().getCaption());
        if(type() == PHOTO) return UpdateRequestFactory.resolve(update.getMessage().getCaption());
        throw new IllegalStateException("unknown update type (2)");
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
        throw new IllegalStateException("지원하지 않는 형태입니다.");
    }
}
