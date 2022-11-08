package infoqoch.telegram.framework.update.request;

import infoqoch.telegram.framework.update.exception.TelegramServerException;
import infoqoch.telegram.framework.update.request.body.UpdateChat;
import infoqoch.telegram.framework.update.request.body.UpdateDataType;
import infoqoch.telegram.framework.update.request.body.UpdateDocument;
import infoqoch.telegrambot.bot.entity.Update;
import lombok.extern.slf4j.Slf4j;

import static infoqoch.telegram.framework.update.request.body.UpdateDataType.*;

@Slf4j
public class UpdateRequest {
    private final Update update;
    private UpdateRequestCommandAndValue updateRequestCommandAndValue;

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

    public UpdateRequestCommandAndValue updateRequestCommandAndValue() {
        return updateRequestCommandAndValue;
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
                /*", text =" + extractInputText() +*/
                ", body=" + findBodyByDataType() +
                '}';
    }

    private String extractInputText() {
        if(updateDataType() == CHAT) return update.getMessage().getText();
        if(updateDataType() == DOCUMENT) return update.getMessage().getCaption();
        if(updateDataType() == PHOTO) return update.getMessage().getCaption();
        throw new TelegramServerException("unknown update type (3)");
    }


    public String input() {
        return extractInputText();
    }

    public void setupCommand(UpdateRequestCommand command) {
        String value = command.extractValue(extractInputText());
        updateRequestCommandAndValue = new UpdateRequestCommandAndValue(command, value);
        log.info("this updateRequestCommandAndValue : {}", updateRequestCommandAndValue);
    }
}
