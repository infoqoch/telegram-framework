package infoqoch.dictionarybot;

import infoqoch.dictionarybot.send.request.SendRequest;
import infoqoch.dictionarybot.system.event.Events;
import infoqoch.dictionarybot.update.exception.TelegramException;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.log.UpdateLog;
import infoqoch.dictionarybot.update.log.repository.UpdateLogRepository;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.TelegramUpdate;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static infoqoch.dictionarybot.send.SendType.MESSAGE;

@Slf4j
@Component
public class DictionaryUpdateRunner {

    private final TelegramUpdate updater;
    private final UpdateDispatcher updateDispatcher;
    private final UpdateLogRepository updateLogRepository;
    private long LAST_UPDATE_ID = 0l;

    public DictionaryUpdateRunner(TelegramBot telegramBot, UpdateDispatcher updateDispatcher, UpdateLogRepository updateLogRepository) {
        this.updater = telegramBot.update();
        this.updateDispatcher = updateDispatcher;
        this.updateLogRepository = updateLogRepository;
    }

    @Scheduled(fixedDelay = 500)
    public void run() {
        final Response<List<Update>> telegramUpdateResponse = updater.get(LAST_UPDATE_ID);

        for (Update update : telegramUpdateResponse.getResult()) {
            upToDateUpdateId(update.getUpdateId());

            handleUpdate(update);
        }
    }

    private void upToDateUpdateId(Long updateId) {
        if (updateId > LAST_UPDATE_ID)
            LAST_UPDATE_ID = updateId;
    }

    private void handleUpdate(Update update) {
        final UpdateRequest updateRequest = new UpdateRequest(update);
        log.debug("updateRequest = {}", updateRequest);

        try{
            final UpdateResponse updateResponse = resolveUpdate(updateRequest);
            log.debug("updateResponse = {}", updateResponse);

            saveUpdateInRepository(updateRequest, updateResponse);

            requestSending(updateRequest, updateResponse);

        } catch (Exception e){
            log.error("[error] handleUpdate, ", e);

            requestSending(updateRequest, updateExceptionHandler(e));
        }
    }

    private UpdateResponse resolveUpdate(UpdateRequest request) {
        try{
            return updateDispatcher.process(request);
        } catch (Exception e){
            log.error("[error] resolveUpdate. ", e);
            return updateExceptionHandler(e);
        }
    }

    private void saveUpdateInRepository(UpdateRequest updateRequest, UpdateResponse updateResponse) {
        updateLogRepository.save(UpdateLog.of(updateRequest, updateResponse));
    }

    private void requestSending(UpdateRequest updateRequest, UpdateResponse updateResponse) {
        Events.raise(new SendRequest(updateRequest.chatId(), updateResponse.sendType(), updateResponse.document(), updateResponse.message()));
    }

    private UpdateResponse updateExceptionHandler(Exception e) {
        final Optional<TelegramException> telegramException = checkIfCausedByTelegramException(e);

        if(telegramException.isPresent()){
            final MarkdownStringBuilder response = telegramException.get().response();

            if (response != null) return new UpdateResponse(MESSAGE, response);

            return new UpdateResponse(MESSAGE,  new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (1)"));
        }
        return new UpdateResponse(MESSAGE, new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (2)"));
    }

    private Optional<TelegramException> checkIfCausedByTelegramException(Throwable e) {
        if(e instanceof TelegramException) return Optional.of((TelegramException)e);

        if(e.getCause() != null) checkIfCausedByTelegramException(e.getCause());

        return Optional.empty();
    }
}
