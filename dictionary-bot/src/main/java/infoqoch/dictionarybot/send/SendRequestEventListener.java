package infoqoch.dictionarybot.send;

import infoqoch.dictionarybot.main.AdminUserRunner;
import infoqoch.dictionarybot.main.SendRunner;
import infoqoch.dictionarybot.send.repository.SendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendRequestEventListener {
	private final SendRepository store;
    private final SendRunner sendRunner;
    private final AdminUserRunner adminUserRunner;

    // 사실상 SendRunner는 여기서 호출함. DB에 저장하는 이유는 에러 및 로깅을 위해서임.
    // 그러므로 db에 저장한 후, runner를 바로 호출하면 됨. 굳이 스케줄러를 계속 동작시킬 필요는 없음.
    // 스케줄러는 혹시 모르므로 100초에 한 번씩 호출하도록 함.
    @EventListener(Send.class)
    public void handle(Send send) {
        store.save(send);
        sendRunner.run();
        if(send.getRequest().getSendType() == SendType.SERVER_ERROR)
            adminUserRunner.run();
    }
}
