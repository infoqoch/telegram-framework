package infoqoch.dictionarybot.update.request;

import infoqoch.dictionarybot.update.exception.TelegramServerException;
import infoqoch.dictionarybot.update.request.body.UpdateChat;
import infoqoch.dictionarybot.update.request.body.UpdateDataType;
import infoqoch.dictionarybot.update.request.body.UpdateDocument;
import infoqoch.telegrambot.bot.entity.Update;

import static infoqoch.dictionarybot.update.request.body.UpdateDataType.*;

public class UpdateRequest {
    private final Update update;

    public UpdateRequest(Update update) {
        this.update = update;
    }

    public Long chatId(){
        return toChat().getChat().getId();
    }

    public Long updateId(){
        return update.getUpdateId();
    }

    public UpdateDataType updateDataType() {
        if(update.getEditedMessage()!=null) return EDITED;
        if(update.getMessage().getPhoto()!=null) return PHOTO;
        if(update.getMessage().getDocument()!=null) return DOCUMENT;
        if(update.getMessage().getText()!=null) return CHAT;
        throw new TelegramServerException("unknown update type (1)");
    }

    public UpdateRequestCommand command(){
        return updateRequestMessage().getCommand();
    }

    public String value(){
        return updateRequestMessage().getValue();
    }

    public UpdateRequestMessage updateRequestMessage() {
        if(updateDataType() == CHAT) return UpdateRequestMessageParser.resolve(update.getMessage().getText());
        if(updateDataType() == DOCUMENT) return UpdateRequestMessageParser.resolve(update.getMessage().getCaption());
        if(updateDataType() == PHOTO) return UpdateRequestMessageParser.resolve(update.getMessage().getCaption());
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

    public Object findBodyByType(Class<?> type) {
        if(type == UpdateChat.class) return toChat();
        if(type == UpdateDocument.class) return toDocument();
        throw new TelegramServerException("not support data type (4)");
    }

    public Object findBodyByDataType() {
        if(updateDataType() == CHAT) return toChat();
        if(updateDataType() == DOCUMENT) return toDocument();
        return null;
    }

    @Override
    public String toString() {
        return "UpdateRequest{" +
                "chatId=" + chatId() +
                ", updateRequestMessage=" + updateRequestMessage() +
                ", body=" + findBodyByDataType() +
                '}';
    }
}
