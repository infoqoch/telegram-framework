package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.update.exception.TelegramServerException;
import infoqoch.dictionarybot.update.log.UpdateLog;
import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendDocumentRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

import static infoqoch.dictionarybot.send.Send.Status.*;
import static javax.persistence.FetchType.LAZY;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Send {
    @Id @GeneratedValue
    private Long no;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "update_log_no")
    private UpdateLog updateLog;

    @Embedded
    private SendRequest request;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String errorCode;
    private String errorMessage;

    @Builder
    public Send(Long no, SendRequest request, UpdateLog updateLog, Status status) {
        this.no = no;
        this.request = request;
        this.updateLog = updateLog;
        this.status = status;
    }

    public Send(Long no, SendRequest request, Status status) {
        this.no = no;
        this.request = request;
        this.status = status;
    }

    public enum Status{
        REQUEST, SENDING, SUCCESS, RESPONSE_ERROR, ERROR;
    }

    public static Send of(SendRequest request, UpdateLog updateLog) {
        return Send.builder()
                .request(request)
                .updateLog(updateLog)
                .status(REQUEST)
                .build();
    }

    public static Send of(SendRequest request) {
        return Send.builder()
                .request(request)
                .updateLog(null)
                .status(REQUEST)
                .build();
    }

    // 상태 전달
    public Status status() {
        return status;
    }

    // 발송 결과
    public SendResult result(){
        return new SendResult(status, errorCode, errorMessage, new SendRequest(request));
    }

    // 실제 발송 로직
    public void sending(TelegramSend telegramSend){
        this.status = SENDING;
        try {
            Response<?> sendResponse = sendDispatcher(telegramSend); // telegram-bot의 응답 데이터를 받는다. 이하 이를 분석하고 결과값을 전달한다.
            resolveResponse(sendResponse);
        }catch (Exception e) {
            log.error("[error : {}], ", "DictionarySendRunner", e);
            error(e);
        }
    }

    private Response<?> sendDispatcher(TelegramSend telegramSend) {
        if(request.sendType() == SendType.MESSAGE) return telegramSend.message(new SendMessageRequest(request.chatId(), request.message()));
        if(request.sendType() == SendType.DOCUMENT) return telegramSend.document(new SendDocumentRequest(request.chatId(), request.document(), request.message()));
        throw new TelegramServerException("not supported SendType");
    }

    private void resolveResponse(Response response) {
        if(response.isOk()){
            success(response);
        }else{
            responseError(response);
        }
    }

    private void responseError(Response response) {
        log.error("[response_error] [{}] {}", errorCode, errorMessage);

        this.status = RESPONSE_ERROR;
        this.errorCode = response.getErrorCode();
        this.errorMessage = response.getDescription();
    }

    private void success(Response response) {
        this.status = SUCCESS;
    }

    private void error(Exception e) {
        this.status = ERROR;
        this.errorMessage = e.getMessage();
    }
}