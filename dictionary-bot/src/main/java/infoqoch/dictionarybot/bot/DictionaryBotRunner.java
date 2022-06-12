package infoqoch.dictionarybot.bot;

import infoqoch.dictionarybot.send.SendDispatcher;
import infoqoch.dictionarybot.send.request.SendRequest;
import infoqoch.dictionarybot.send.response.SendResponse;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.request.UpdateWrapper;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.TelegramUpdate;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DictionaryBotRunner {

    private final TelegramUpdate updater;
    private final UpdateDispatcher updateDispatcher;
    private final SendDispatcher sendDispatcher;
    private long LAST_UPDATE_ID = 0l;

    public DictionaryBotRunner(TelegramBot telegramBot, UpdateDispatcher updateDispatcher, SendDispatcher sendDispatcher) {
        this.updater = telegramBot.update();
        this.updateDispatcher = updateDispatcher;
        this.sendDispatcher = sendDispatcher;
    }

    @Scheduled(fixedDelay = 500)
    public void run() {
        final Response<List<Update>> telegramUpdateResponse = updater.get(LAST_UPDATE_ID);

        for (Update update : telegramUpdateResponse.getResult()) {
            replaceLastUpdateId(update.getUpdateId());

            handleUpdate(update);
        }
    }

    private void handleUpdate(Update update) {
        final UpdateWrapper updateWrap = new UpdateWrapper(update);
        final UpdateResponse updateResponse = resolveUpdate(updateWrap);
        log.debug("updateResponse = {}", updateResponse);

        final SendResponse sendResponse = resolveSend(updateWrap, updateResponse);
        log.debug("sendResponse = {}", sendResponse);
    }

    private SendResponse resolveSend(UpdateWrapper updateWrap, UpdateResponse updateResponse) {
        try{
            final SendRequest request = new SendRequest(updateWrap.chatId(), updateResponse.type(), updateResponse.document(), updateResponse.body());
            return sendDispatcher.process(request);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private UpdateResponse resolveUpdate(UpdateWrapper updateWrap) {
        try{
            return updateDispatcher.process(updateWrap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new UpdateResponse(SendType.MESSAGE, "알 수 없는 오류가 발생하였습니다!");
    }

    private void replaceLastUpdateId(Long updateId) {
        if (updateId > LAST_UPDATE_ID)
            LAST_UPDATE_ID = updateId;
    }
}
