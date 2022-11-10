package infoqoch.dictionarybot.log;

import infoqoch.dictionarybot.log.send.SendLog;
import infoqoch.dictionarybot.log.send.repository.SendRepository;
import infoqoch.dictionarybot.log.update.repository.UpdateLogRepository;
import infoqoch.telegram.framework.update.send.Send;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendResultSubscribeEventListener {
    private final SendRepository sendRepository;
    private final UpdateLogRepository updateLogRepository;

    @Async
    @EventListener(Send.class)
    public void handle(Send send) {
        while(!send.isDone());

        SendLog sendLog = SendLog.of(send);
        if(sendLog.getUpdateLog()!=null){
            updateLogRepository.save(sendLog.getUpdateLog());
        }
        sendRepository.save(sendLog);
    }
}