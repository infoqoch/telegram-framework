package infoqoch.mock.bot;

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
    private String mockMessageResponseJson;
    private String mockDocumentResponseJson;
    boolean isDocumentCalled = false;
    boolean isMessageCalled = false;

    Response<SendMessageResponse> messageResult;
    private Response<SendDocumentResponse> documentResult;
    private SendMessageRequest sendMessageRequest;
    private SendDocumentRequest sendDocumentRequest;

    private boolean throwRuntimeException;

    @Override
    public Response<SendMessageResponse> message(SendMessageRequest sendMessageRequest) {
        if(throwRuntimeException) throw new RuntimeException("예외닷!");

        this.sendMessageRequest = sendMessageRequest;
        isMessageCalled = true;
        messageResult = DefaultJsonBind.getInstance().toObject(mockMessageResponseJson, SendMessageResponse.class);
        return messageResult;
    }

    @Override
    public Response<SendDocumentResponse> document(SendDocumentRequest sendDocumentRequest) {
        if(throwRuntimeException) throw new RuntimeException("예외닷!");

        this.sendDocumentRequest = sendDocumentRequest;
        isDocumentCalled = true;
        documentResult = DefaultJsonBind.getInstance().toObject(mockDocumentResponseJson, SendDocumentResponse.class);
        return documentResult;

    }
}

