package infoqoch.dictionarybot.update;

import infoqoch.dictionarybot.update.exception.TelegramException;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.request.UpdateRequestCommand;
import infoqoch.dictionarybot.update.resolver.UpdateRequestMethodResolver;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static infoqoch.dictionarybot.update.response.SendType.SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
public class UpdateDispatcher {
    private final Map<UpdateRequestCommand, UpdateRequestMethodResolver> methodResolvers;

    public UpdateResponse process(UpdateRequest updateRequest) {
        log.debug("updateRequest = {}", updateRequest);
        try{
            final UpdateRequestCommand command = getUpdateRequestCommand(updateRequest);
            updateRequest.setupWithCommand(command);

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
        }

        return commands.stream().sorted(Comparator.comparingInt(o -> o.get().length())).toList().get(0);
    }

    private UpdateResponse exceptionHandler(Exception e) {
        final Optional<TelegramException> telegramException = TelegramException.checkIfCausedByTelegramException(e);

        if(telegramException.isPresent())
            return UpdateResponse.error(telegramException.get(), new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (1)"));

        return UpdateResponse.send(SERVER_ERROR, new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (2)"));
    }
}