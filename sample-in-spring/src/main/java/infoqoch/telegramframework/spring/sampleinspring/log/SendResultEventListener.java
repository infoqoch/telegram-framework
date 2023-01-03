package infoqoch.telegramframework.spring.sampleinspring.log;

import infoqoch.telegram.framework.update.send.Send;
import infoqoch.telegram.framework.update.send.SendResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// telegram.send-message-after-update-resolved=true 일 경우 UpdateRunner가 동작한다.
// UpdateRunner 는 UpdateDispatcher 가 생성한 값을 텔래그램 api로 보낸다.
// 이때 이벤트 퍼블리셔인 SendUpdateResponseEventListener 를 통해 처리하며 이때 리스닝하는 타입이 Send 이다.
// Send는 CompletableFuture를 감싼 isDone() 메서드로 완료 여부를 리턴한다. 이를 리스닝하는 리스너는 반드시 isDone()을 확인해야 한다.
// 반드시 @Async 를 붙여야 한다. 비동기로 처리하여 SubscribeSendUpdateEventListener가 SendUpdateResponseEventListener보다 먼저 동작할 경우 무한루프에 빠지게 된다.
@Slf4j
@Component
@RequiredArgsConstructor
public class SendResultEventListener {
    private final SendLogRepository sendLogRepository;
    private final UpdateLogRepository updateLogRepository;

    @Async
    @Transactional
    @EventListener(SendResult.class)
    public void handle(SendResult sendResult) {
        Send send = sendResult.getSend();
        UpdateLog updateLog = ifUpdateLogExistThenSave(send);

        SendLog sendLog = SendLog.of(send, updateLog);
        sendLogRepository.save(sendLog);
    }

    private UpdateLog ifUpdateLogExistThenSave(Send send) {
        UpdateLog updateLog = null;
        if(send.getUpdateRequest().isPresent()){
            updateLog = UpdateLog.of(send.getUpdateRequest().get().updateId(), send.getUpdateRequest().get(), send.getUpdateResponse().get());
            updateLogRepository.save(updateLog);
        }
        return updateLog;
    }
}
