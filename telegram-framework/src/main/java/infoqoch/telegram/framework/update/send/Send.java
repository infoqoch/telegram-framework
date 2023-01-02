package infoqoch.telegram.framework.update.send;

import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.response.ResponseType;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendDocumentRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static infoqoch.telegram.framework.update.send.Send.Status.*;

@Slf4j
@Getter
@ToString
public class Send {
    private final Long chatId;
    private final ResponseType responseType;
    private final MarkdownStringBuilder message;
    private final String document;

    private Status status;
    private int errorCode;
    private String errorMessage;

    private Optional<Send> resend = Optional.empty();

    private Optional<UpdateRequest> updateRequest = Optional.empty();
    private Optional<UpdateResponse> updateResponse = Optional.empty();

    public enum Status {
        REQUEST, SENDING, SUCCESS, RESPONSE_ERROR, ERROR;
    }

    private Send(Long chatId, ResponseType responseType, String document, MarkdownStringBuilder msb) {
        this.chatId = chatId;
        this.responseType = responseType;
        this.document = document;
        this.message = msb;
    }

    public static Send sendMessage(long chatId, MarkdownStringBuilder msb) {
        return new Send(chatId, ResponseType.MESSAGE, null, msb);
    }

    public static Send sendDocument(Long chatId, String document, MarkdownStringBuilder msb) {
        return new Send(chatId, ResponseType.DOCUMENT, document, msb);
    }

    public static Send send(Long chatId, ResponseType responseType, MarkdownStringBuilder message, String document) {
        return new Send(chatId, responseType, document, message);
    }

    public void setupUpdate(Long updateId, UpdateRequest updateRequest, UpdateResponse updateResponse){
        this.updateRequest = Optional.ofNullable(updateRequest);
        this.updateResponse = Optional.ofNullable(updateResponse);
    }

    public MarkdownStringBuilder getMessage() {
        return new MarkdownStringBuilder().append(message);
    }

    // 실제 발송 로직
    public void sending(TelegramSend telegramSend){
        this.status = SENDING;
        send(telegramSend);
    }

    public void resending(ResponseType type, MarkdownStringBuilder msb, TelegramSend telegramSend){
        resend = Optional.of(Send.send(chatId, ResponseType.SERVER_ERROR, msb, null));
        resend.get().send(telegramSend);
    }

    private void send(TelegramSend telegramSend) {
        try {
            Response<?> sendResponse = sendDispatcher(telegramSend); // telegram-bot의 응답 데이터를 받는다. 이하 이를 분석하고 결과값을 전달한다.
            resolveResponse(sendResponse);
        }catch (Exception e) {
            error(e);
            throw e;
        }
    }

    private Response<?> sendDispatcher(TelegramSend telegramSend) {
        if(getResponseType() == ResponseType.DOCUMENT) return telegramSend.document(new SendDocumentRequest(getChatId(), getDocument(), getMessage()));
        return telegramSend.message(new SendMessageRequest(getChatId(), getMessage()));
    }

    private void resolveResponse(Response response) {
        if(response.isOk()) success(response);
        else responseError(response);
    }

    private void success(Response response) {
        this.status = SUCCESS;
    }

    private void responseError(Response response) {
        this.status = RESPONSE_ERROR;
        this.errorCode = response.getErrorCode();
        this.errorMessage = response.getDescription();

        log.error("[response_error] [{}] {}", errorCode, errorMessage);
    }

    private void error(Exception e) {
        this.status = ERROR;
        this.errorMessage = e.getMessage();
    }
}