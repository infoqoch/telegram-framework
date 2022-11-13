package infoqoch.telegramframework.spring.simpleinspring.controller;

import infoqoch.telegram.framework.update.UpdateRequestMapper;
import infoqoch.telegram.framework.update.request.UpdateRequestCommandAndValue;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.springframework.stereotype.Component;

@Component
public class BaseController {
    // * 은 반드시 구현해야 한다. 어떤 명령어에도 해당하지 않는 채팅메시지에 대응한다.
    @UpdateRequestMapper({"*", "help"})
    public MarkdownStringBuilder help(){
        return new MarkdownStringBuilder()
                .plain("안녕하세요! 현재 제공하는 기능은 다음과 같습니다.").lineSeparator()
                .command("help", null).plain(" -> 도움말").lineSeparator()
                .command("echo", "echo").plain(" -> 입력한 값을 메아리로 돌려준다").lineSeparator()
                .plain("명령을 할 때 슬러시('/')는 생략 가능하며 언더바('_')는 띄어쓰기(' ')로 대체합니다.");
    }

    @UpdateRequestMapper({"echo", "에코", "애코", "메아리", "야호"})
    public MarkdownStringBuilder echo(UpdateRequestCommandAndValue commandAndValue){
        return new MarkdownStringBuilder().italic(commandAndValue.getValue()+"~~~");
    }
}
