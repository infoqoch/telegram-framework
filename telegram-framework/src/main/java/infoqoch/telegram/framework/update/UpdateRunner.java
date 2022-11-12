package infoqoch.telegram.framework.update;

import infoqoch.telegram.framework.update.event.Events;
import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import infoqoch.telegram.framework.update.send.Send;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.TelegramUpdate;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
public class UpdateRunner {
    private final TelegramUpdate updater;
    private final UpdateDispatcher updateDispatcher;

    private long LAST_UPDATE_ID = 0l;

    public UpdateRunner(TelegramBot telegramBot, UpdateDispatcher updateDispatcher) {
        this.updater = telegramBot.update();
        this.updateDispatcher = updateDispatcher;
    }

    @Scheduled(fixedDelay = 100)
    public void run() {
        final Response<List<Update>> telegramUpdateResponse = updater.get(LAST_UPDATE_ID);
        log.info("telegramUpdateResponse {}" , telegramUpdateResponse);

        for (Update update : telegramUpdateResponse.getResult()) {
            upToDateUpdateId(update.getUpdateId());
            final UpdateRequest updateRequest = new UpdateRequest(update);

            UpdateResponse updateResponse = updateDispatcher.process(updateRequest);

            requestSending(updateRequest, updateResponse);
        }
    }

    private void upToDateUpdateId(Long updateId) {
        if (updateId > LAST_UPDATE_ID) LAST_UPDATE_ID = updateId;
    }

    private void requestSending(UpdateRequest updateRequest, UpdateResponse updateResponse) {
        final Send send = Send.send(updateRequest.chatId(), updateResponse.getResponseType(), updateResponse.getMessage(), updateResponse.getDocument());
        send.setupUpdate(updateRequest.updateId(), updateRequest, updateResponse);
        Events.raise(send);
    }
}
