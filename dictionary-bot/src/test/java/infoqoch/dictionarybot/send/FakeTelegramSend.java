package infoqoch.dictionarybot.send;

import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendDocumentRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.bot.response.SendDocumentResponse;
import infoqoch.telegrambot.bot.response.SendMessageResponse;
import infoqoch.telegrambot.util.DefaultJsonBind;
import lombok.Data;

@Data
public class FakeTelegramSend implements TelegramSend {
    private String messageResponseJson;
    private String documentResponseJson;

    @Override
    public Response<SendMessageResponse> message(SendMessageRequest sendMessageRequest) {
        return new DefaultJsonBind().toObject(messageResponseJson, SendMessageResponse.class);
    }

    boolean isDocumentCalled = false;

    @Override
    public Response<SendDocumentResponse> document(SendDocumentRequest sendDocumentRequest) {
        isDocumentCalled = true;
        return new DefaultJsonBind().toObject(documentResponseJson, SendDocumentResponse.class);

    }
}

