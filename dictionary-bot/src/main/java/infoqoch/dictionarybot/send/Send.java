package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.update.log.UpdateLog;
import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendDocumentRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

import static infoqoch.dictionarybot.send.Send.Status.*;
import static javax.persistence.FetchType.LAZY;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    public enum Status{
        REQUEST, SENDING, SUCCESS, RESPONSE_ERROR, ERROR;
    }

    // 생성자

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

    public SendResult result(){
        return new SendResult(status, errorCode, errorMessage, request.copy());
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
        if(request.getSendType() == SendType.DOCUMENT) return telegramSend.document(new SendDocumentRequest(request.getChatId(), request.getDocument(), request.getMessage()));
        return telegramSend.message(new SendMessageRequest(request.getChatId(), request.getMessage()));
        // throw new TelegramServerException("not supported SendType");
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