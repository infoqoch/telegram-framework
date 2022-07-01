package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.send.request.SendRequest;
import infoqoch.dictionarybot.send.response.SendResponse;
import infoqoch.dictionarybot.system.exception.TelegramErrorResponseException;
import infoqoch.dictionarybot.system.exception.TelegramServerException;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendDocumentRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;

public class SendDispatcher {
    private final TelegramSend send;

    public SendDispatcher(TelegramBot bot) {
        this.send = bot.send();
    }

    public SendResponse process(SendRequest request) {
        Response<?> response = send(request);
        if(response.getErrorCode()!=null){
            throw new TelegramErrorResponseException(response.getErrorCode(), response.getDescription());
        }
        return new SendResponse(response.isOk(), response.getResult());
    }

    private Response<?> send(SendRequest request) {
        if(request.sendType() == SendType.MESSAGE) return send.message(new SendMessageRequest(request.chatId(), request.message()));
        if(request.sendType() == SendType.DOCUMENT) return send.document(new SendDocumentRequest(request.chatId(), request.document(), request.message()));
        throw new TelegramServerException("not supported SendType");
    }
}
