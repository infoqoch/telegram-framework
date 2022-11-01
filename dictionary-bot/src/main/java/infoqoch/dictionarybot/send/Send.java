package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.log.UpdateLog;
import infoqoch.dictionarybot.update.response.SendType;
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

    private int errorCode;
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
            error(e);
            throw e;
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
            // TODO. 이 부분을 예외로 던져야 하는가?
            // 서버에서 처리할 수 없는 에러(chat not found : 응답할 수 없는 채팅방)과 처리할 수 있는 예외(markdown 문법 에러 : 서버에러로 응답해야함)로 분리할 수 있음.
            // 전자의 경우 예외를 던지고 다시 메시지를 보내는 과정에서 에러가 발생할 수 있다는 가능성을 우회할 수 없음. 결과적으로 Telegram의 에러 응답에 대한 정리를 필요로 할 수 있음. 하지만 이에 대한 스펙이 정확하게 정리된 것으로 보이지는 않음.
            // 일단 할 일로 남겨 둔다. 이후 정리한다.
            // throw new IllegalStateException("텔래그램에서 400 이상으로 응답하였습니다. code : "+errorCode+", message :"+errorMessage);
        }
    }

    private void responseError(Response response) {
        this.status = RESPONSE_ERROR;
        this.errorCode = response.getErrorCode();
        this.errorMessage = response.getDescription();

        log.error("[response_error] [{}] {}", errorCode, errorMessage);
    }

    private void success(Response response) {
        this.status = SUCCESS;
    }

    private void error(Exception e) {
        this.status = ERROR;
        this.errorMessage = e.getMessage();
    }
}