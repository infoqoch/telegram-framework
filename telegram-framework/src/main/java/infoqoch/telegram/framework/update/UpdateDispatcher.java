package infoqoch.telegram.framework.update;

import infoqoch.telegram.framework.update.exception.TelegramException;
import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.request.UpdateRequestCommand;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static infoqoch.telegram.framework.update.response.ResponseType.SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
public class UpdateDispatcher {
    private final Map<UpdateRequestCommand, UpdateRequestResolver> methodResolvers;

    @Transactional
    public UpdateResponse process(UpdateRequest updateRequest) {
        log.debug("updateRequest = {}", updateRequest);
        try{
            final UpdateRequestCommand command = getUpdateRequestCommand(updateRequest);
            updateRequest.setupCommand(command);
            return methodResolvers.get(command).process(updateRequest);
        } catch (Exception e){
            log.error("[error] failed resolve update ", e);
            return exceptionHandler(e);
        }
    }

    private UpdateRequestCommand getUpdateRequestCommand(UpdateRequest updateRequest) {
        final List<UpdateRequestCommand> commands = methodResolvers.keySet().stream()
                .filter(c -> c.startsWith(updateRequest.input()))
                .toList();

        if(commands.size()==0){
            return UpdateRequestCommand.of("*");
        }else if(commands.size()==1){
            return commands.get(0);
        }else{
            return takeLongestResolver(commands);
        }
    }

    // 명령어에 여러 개의 리졸버가 해당될 경우, 가장 긴 명령어를 가진 리졸버를 사용한다.
    private static UpdateRequestCommand takeLongestResolver(List<UpdateRequestCommand> commands) {
        return commands.stream().sorted(Comparator.comparingInt(o -> -o.get().length())).toList().get(0);
    }

    private UpdateResponse exceptionHandler(Exception e) {
        final Optional<TelegramException> telegramException = TelegramException.checkIfCausedByTelegramException(e);

        if(telegramException.isPresent())
            return UpdateResponse.error(telegramException.get(), new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (1)"));

        return UpdateResponse.send(SERVER_ERROR, new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (2)"));
    }
}