package infoqoch.telegramframework.spring.sampleinspring.controller;

import infoqoch.telegram.framework.update.UpdateRequestMapper;
import infoqoch.telegram.framework.update.request.UpdateRequestCommandAndValue;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BaseController {
    // * 은 반드시 구현해야 한다. 어떤 명령어에도 해당하지 않는 채팅메시지에 대응한다.
    @UpdateRequestMapper({"*", "help"})
    public String help(){
        return "안녕!";
    }

    @UpdateRequestMapper({"echo", "에코", "애코", "메아리", "야호"})
    public MarkdownStringBuilder echo(UpdateRequestCommandAndValue commandAndValue){
        return new MarkdownStringBuilder().italic(commandAndValue.getValue()+"~~~");
    }
    
    @UpdateRequestMapper({"dt", "time", "시간"})
    public LocalDateTime datetime(){
        return LocalDateTime.now();
    }
}
