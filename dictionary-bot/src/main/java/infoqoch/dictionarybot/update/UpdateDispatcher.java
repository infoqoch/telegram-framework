package infoqoch.dictionarybot.update;

import infoqoch.dictionarybot.update.resolver.UpdateRequestMethodResolver;
import infoqoch.dictionarybot.update.exception.TelegramException;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static infoqoch.dictionarybot.update.response.SendType.SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
public class UpdateDispatcher {
    private final List<UpdateRequestMethodResolver> methodResolvers;

    public UpdateResponse process(UpdateRequest updateRequest) {
        log.debug("updateRequest = {}", updateRequest);
        try{
            final UpdateResponse response = methodResolvers.stream()
                    .filter(r -> r.support(updateRequest)).findAny().orElseThrow(() -> new IllegalStateException("fatal error!! can not resolve this updateRequest : " + updateRequest.toString()))
                    .process(updateRequest);
            log.info("resolved update result : {}", response);
            return response;
        } catch (Exception e){
            log.error("[error] failed resolve update ", e);
            return exceptionHandler(e);
        }
    }

    private UpdateResponse exceptionHandler(Exception e) {
        final Optional<TelegramException> telegramException = TelegramException.checkIfCausedByTelegramException(e);

        if(telegramException.isPresent())
            return UpdateResponse.error(telegramException.get(), new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (1)"));

        return UpdateResponse.send(SERVER_ERROR, new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (2)"));
    }
}