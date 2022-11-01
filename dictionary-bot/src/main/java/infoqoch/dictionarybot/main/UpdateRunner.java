package infoqoch.dictionarybot.main;

import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.SendRequest;
import infoqoch.dictionarybot.system.event.Events;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.log.UpdateLog;
import infoqoch.dictionarybot.log.repository.UpdateLogRepository;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.TelegramUpdate;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@Transactional
public class UpdateRunner {

    private final TelegramUpdate updater;
    private final UpdateDispatcher updateDispatcher;
    private final UpdateLogRepository updateLogRepository;

    private long LAST_UPDATE_ID = 0l;

    public UpdateRunner(TelegramBot telegramBot, UpdateDispatcher updateDispatcher, UpdateLogRepository updateLogRepository) {
        this.updater = telegramBot.update();
        this.updateDispatcher = updateDispatcher;
        this.updateLogRepository = updateLogRepository;
    }

    @Scheduled(fixedDelay = 500)
    public void run() {
        final Response<List<Update>> telegramUpdateResponse = updater.get(LAST_UPDATE_ID);

        for (Update update : telegramUpdateResponse.getResult()) {
            upToDateUpdateId(update.getUpdateId());
            final UpdateRequest updateRequest = new UpdateRequest(update);

            UpdateResponse updateResponse = updateDispatcher.process(updateRequest);

            final UpdateLog updateLog = saveUpdateInRepository(updateRequest, updateResponse);
            requestSending(updateRequest, updateResponse, updateLog);
        }
    }

    private void upToDateUpdateId(Long updateId) {
        if (updateId > LAST_UPDATE_ID)
            LAST_UPDATE_ID = updateId;
    }

    private UpdateLog saveUpdateInRepository(UpdateRequest updateRequest, UpdateResponse updateResponse) {
        return updateLogRepository.save(UpdateLog.of(updateRequest, updateResponse));
    }

    private void requestSending(UpdateRequest updateRequest, UpdateResponse updateResponse, UpdateLog updateLog) {
        final SendRequest sendRequest = SendRequest.send(updateRequest.chatId(), updateResponse.getSendType(), updateResponse.getMessage(), updateResponse.getDocument());
        final Send send = Send.of(sendRequest, updateLog);
        Events.raise(send);
    }
}
