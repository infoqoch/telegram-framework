package infoqoch.telegram.framework.update.send;

import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.response.SendType;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendDocumentRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static infoqoch.telegram.framework.update.send.Send.Status.*;


@Getter
@Slf4j
public class Send {
    private final String id = UUID.randomUUID().toString();

    private final Long chatId;
    private final SendType sendType;
    private final MarkdownStringBuilder message;
    private final String document;

    private Status status;
    private int errorCode;
    private String errorMessage;

    private Optional<Send> resend;

    private Optional<Long> updateId;
    private Optional<UpdateRequest> updateRequest;
    private Optional<UpdateResponse> updateResponse;

    public CompletableFuture<Boolean> job = new CompletableFuture<>();

    public boolean isDone(){
        return job.isDone();
    }

    public void done() {
        job.complete(true);
    }

    public enum Status {
        REQUEST, SENDING, SUCCESS, RESPONSE_ERROR, ERROR;
    }

    private Send(Long chatId, SendType sendType, String document, MarkdownStringBuilder msb) {
        this.chatId = chatId;
        this.sendType = sendType;
        this.document = document;
        this.message = msb;
    }

    public static Send sendMessage(long chatId, MarkdownStringBuilder msb) {
        return new Send(chatId, SendType.MESSAGE, null, msb);
    }

    public static Send sendDocument(Long chatId, String document, MarkdownStringBuilder msb) {
        return new Send(chatId, SendType.DOCUMENT, document, msb);
    }

    public static Send send(Long chatId, SendType sendType, MarkdownStringBuilder message, String document) {
        return new Send(chatId, sendType, document, message);
    }

    public void setupUpdate(Long updateId, UpdateRequest updateRequest, UpdateResponse updateResponse){
        this.updateId = Optional.ofNullable(updateId);
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

    public void resending(SendType type, MarkdownStringBuilder msb, TelegramSend telegramSend){
        resend = Optional.of(Send.send(chatId,SendType.SERVER_ERROR, msb, null));
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
        if(getSendType() == SendType.DOCUMENT) return telegramSend.document(new SendDocumentRequest(getChatId(), getDocument(), getMessage()));
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