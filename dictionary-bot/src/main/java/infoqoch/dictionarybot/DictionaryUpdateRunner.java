package infoqoch.dictionarybot;

import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.SendRequest;
import infoqoch.dictionarybot.send.SendType;
import infoqoch.dictionarybot.system.event.Events;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.exception.TelegramClientException;
import infoqoch.dictionarybot.update.exception.TelegramException;
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

import static infoqoch.dictionarybot.send.SendType.CLIENT_ERROR;
import static infoqoch.dictionarybot.send.SendType.SERVER_ERROR;

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

            final UpdateLog updateLog = saveUpdateInRepository(updateRequest, updateResponse);

            requestSending(updateRequest, updateResponse, updateLog);

        } catch (Exception e){
            log.error("[error] handleUpdate, ", e);
            final UpdateResponse updateResponse = updateExceptionHandler(e);
            requestSending(updateRequest, updateResponse, null);
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

    private UpdateLog saveUpdateInRepository(UpdateRequest updateRequest, UpdateResponse updateResponse) {
        return updateLogRepository.save(UpdateLog.of(updateRequest, updateResponse));
    }

    private void requestSending(UpdateRequest updateRequest, UpdateResponse updateResponse, UpdateLog updateLog) {
        final SendRequest sendRequest = SendRequest.send(updateRequest.chatId(), updateResponse.getSendType(), updateResponse.getMessage(), updateResponse.getDocument());
        final Send send = Send.of(sendRequest, updateLog);
        Events.raise(send);
    }

    private UpdateResponse updateExceptionHandler(Exception e) {
        final Optional<TelegramException> opTelegramException = TelegramException.checkIfCausedByTelegramException(e);

        if(opTelegramException.isPresent()){
            final TelegramException telegramException = opTelegramException.get();
            SendType sendType = resolveErrorType(telegramException);

            final MarkdownStringBuilder response = telegramException.response();

            if (response != null) return UpdateResponse.send(sendType, response);

            return UpdateResponse.send(sendType, new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (1)"));
        }

        return UpdateResponse.send(SERVER_ERROR, new MarkdownStringBuilder("서버에 문제가 발생하였습니다. 죄송합니다. (2)"));
    }

    private SendType resolveErrorType(TelegramException telegramException) {
        return telegramException instanceof TelegramClientException ? CLIENT_ERROR: SERVER_ERROR;
    }
}
