package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.send.request.SendRequest;
import infoqoch.dictionarybot.send.response.SendResponse;
import infoqoch.dictionarybot.system.exception.TelegramServerException;
import infoqoch.dictionarybot.update.response.SendType;
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
        Response<?> response = sendResolver(request);
        return new SendResponse(response.isOk(), response.getResult());
    }

    private Response<?> sendResolver(SendRequest request) {
        if(request.type() == SendType.MESSAGE) return send.message(new SendMessageRequest(request.chatId(), request.message()));
        if(request.type() == SendType.DOCUMENT) return send.document(new SendDocumentRequest(request.chatId(), request.document(), request.message()));
        throw new TelegramServerException("not supported SendType");
    }
}
