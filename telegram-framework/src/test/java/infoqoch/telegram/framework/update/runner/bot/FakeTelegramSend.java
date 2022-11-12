package infoqoch.telegram.framework.update.runner.bot;

import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendDocumentRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.bot.response.SendDocumentResponse;
import infoqoch.telegrambot.bot.response.SendMessageResponse;

import java.util.ArrayList;
import java.util.List;

public class FakeTelegramSend implements TelegramSend {
    public Response<SendMessageResponse> messageResult;
    public Response<SendDocumentResponse> documentResult;
    public List<SendMessageRequest> messageRequests = new ArrayList<>();
    public List<SendDocumentRequest> documentRequests = new ArrayList<>();


    public boolean throwRuntimeException;

    @Override
    public Response<SendMessageResponse> message(SendMessageRequest sendMessageRequest) {
        messageRequests.add(sendMessageRequest);
        if(throwRuntimeException) throw new RuntimeException("예외닷!");
        return messageResult;
    }

    @Override
    public Response<SendDocumentResponse> document(SendDocumentRequest sendDocumentRequest) {
        documentRequests.add(sendDocumentRequest);
        if(throwRuntimeException) throw new RuntimeException("예외닷!");
        return documentResult;
    }
}

