package infoqoch.telegramframework.spring.sampleinspring.controller;

import infoqoch.telegram.framework.update.UpdateRequestMapper;
import infoqoch.telegram.framework.update.request.UpdateRequestCommandAndValue;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import infoqoch.telegramframework.spring.sampleinspring.log.UpdateLog;
import infoqoch.telegramframework.spring.sampleinspring.log.UpdateLogRepository;
import infoqoch.telegramframework.spring.sampleinspring.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserController {
    private final UpdateLogRepository updateLogRepository;

    @UpdateRequestMapper({"status", "상태", "나는"})
    public MarkdownStringBuilder status(User user){
        final List<UpdateLog> updateLogs = updateLogRepository.findByChatId(user.getChatId());

        final MarkdownStringBuilder msb = new MarkdownStringBuilder()
                .bold(user.getName()).plain("님의 상태").lineSeparator()
                .append(updateListMsb(updateLogs)).lineSeparator();

        return msb;
    }
    
    @UpdateRequestMapper({"이름변경", "change name", "name"})
    public MarkdownStringBuilder changeName(User user, UpdateRequestCommandAndValue updateRequestCommandAndValue){
        final String value = updateRequestCommandAndValue.getValue();

        if(value==null || value.length()==0) return new MarkdownStringBuilder().bold("변경하고 싶은 이름을 입력하여 주세요!");

        user.changeName(value);

        if(value.equals("ex")) throw new IllegalStateException("꾸엑!");

        return new MarkdownStringBuilder("이름이 변경되었습니다 -> [").bold(value).plain("]");
    }

    private MarkdownStringBuilder updateListMsb(List<UpdateLog> updateLogs) {
        if(updateLogs.size()==0)
            return new MarkdownStringBuilder("값이 없습니다!");

        final MarkdownStringBuilder msb = new MarkdownStringBuilder();
        for (UpdateLog updateLog : updateLogs) {
            msb.plain("- ").bold(updateLog.getMessage()).lineSeparator();
        }
        return msb;
    }
}