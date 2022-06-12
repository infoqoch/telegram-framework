package infoqoch.dictionarybot.bot;

import infoqoch.dictionarybot.send.SendDispatcher;
import infoqoch.dictionarybot.send.request.SendRequest;
import infoqoch.dictionarybot.send.response.SendResponse;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.request.UpdateWrapper;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.TelegramSend;
import infoqoch.telegrambot.bot.TelegramUpdate;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class ApplicationRunner {

    private final TelegramUpdate updater;
    private final UpdateDispatcher updateDispatcher;
    private final SendDispatcher sendDispatcher;
    private long LAST_UPDATE_ID = 0l;

    public ApplicationRunner(TelegramBot telegramBot, UpdateDispatcher updateDispatcher, SendDispatcher sendDispatcher) {
        this.updater = telegramBot.update();
        this.updateDispatcher = updateDispatcher;
        this.sendDispatcher = sendDispatcher;
    }

    @Scheduled(fixedDelay = 1000)
    public void runner(){
        log.info("scheduler runner start : {}", LAST_UPDATE_ID);

        final Response<List<Update>> telegramUpdateResponse = updater.get(LAST_UPDATE_ID);

        if(telegramUpdateResponse.emptyResult()) return;

        for (Update update : telegramUpdateResponse.getResult()) {
            replaceLastUpdateId(update.getUpdateId());
            try{
                final UpdateWrapper updateWrap = new UpdateWrapper(update);
                final UpdateResponse updateResponse = updateDispatcher.process(updateWrap);
                log.info("updateResponse : {}", updateResponse);

                final SendResponse sendResponse = sendDispatcher.process(new SendRequest(updateWrap.chatId(), updateResponse.type(), updateResponse.document(), updateResponse.body()));
                log.info("sendResponse : {}", sendResponse);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        log.info("scheduler runner done  : {}", LAST_UPDATE_ID);
    }

    private void replaceLastUpdateId(Long updateId) {
        if(updateId>LAST_UPDATE_ID)
            LAST_UPDATE_ID = updateId;
    }
}
