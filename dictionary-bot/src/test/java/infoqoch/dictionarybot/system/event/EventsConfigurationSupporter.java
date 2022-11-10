package infoqoch.dictionarybot.system.event;

import infoqoch.telegram.framework.update.event.Events;
import org.springframework.context.ApplicationContext;

// static method의 경우, 스프링 컨텍스트가 바뀌거나 혹은 유닛 테스트를 진행하더라도, 이전의 컨텍스트의 영향을 받는 것으로 확인함. 실제 운영에서는 특별한 문제가 없음. 환경이 계속 바뀌는 테스트에서 문제 발생.
// 위의 문제를 해소하기 위하여 스태틱 메서드의 필드를 강제로 바꾸는 로직임.
public class EventsConfigurationSupporter {
    public static void replaceApplicationContext(ApplicationContext applicationContext) {
        Events.setPublisher(applicationContext);
    }
}
