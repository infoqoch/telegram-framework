package infoqoch.dictionarybot.bot;

import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.request.UpdateWrapper;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.bot.TelegramBot;
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
// @Component
public class ApplicationRunner {

    private final TelegramUpdate updater;
    private final UpdateDispatcher updateDispatcher;
    private long LAST_UPDATE_ID = 0l;

    public ApplicationRunner(TelegramBot telegramBot, UpdateDispatcher updateDispatcher) {
        this.updater = telegramBot.update();
        this.updateDispatcher = updateDispatcher;
    }

    @Scheduled(fixedDelay = 1000)
    public void runner(){
        log.info("scheduler runner start : {}", LAST_UPDATE_ID);

        final Response<List<Update>> telegramUpdateResponse = updater.get(LAST_UPDATE_ID);

        if(telegramUpdateResponse.emptyResult()) return;

        for (Update update : telegramUpdateResponse.getResult()) {
            replaceLastUpdateId(update.getUpdateId());
            try{
                final UpdateResponse response = updateDispatcher.process(new UpdateWrapper(update));
                System.out.println("response.toString() = " + response.toString());
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
