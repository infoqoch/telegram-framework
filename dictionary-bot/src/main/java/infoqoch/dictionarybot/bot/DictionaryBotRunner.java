package infoqoch.dictionarybot.bot;

import infoqoch.dictionarybot.send.SendDispatcher;
import infoqoch.dictionarybot.send.request.SendRequest;
import infoqoch.dictionarybot.send.response.SendResponse;
import infoqoch.dictionarybot.system.exception.TelegramException;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.request.UpdateWrapper;
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

import static infoqoch.dictionarybot.update.response.SendType.MESSAGE;

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

    private UpdateResponse resolveUpdate(UpdateWrapper updateWrap) {
        try{
            return updateDispatcher.process(updateWrap);
        } catch (Exception e){
            return updateExceptionHandler(e);
        }
    }

    private UpdateResponse updateExceptionHandler(Exception e) {
        final Optional<TelegramException> telegramException = causedByTelegramException(e);
        if(telegramException.isPresent()){
            final TelegramException te = telegramException.get();
            final MarkdownStringBuilder response = te.response();
            if (response != null) return new UpdateResponse(MESSAGE, response);
            return new UpdateResponse(MESSAGE, "서버에 문제가 발생하였습니다. 죄송합니다.");
        }
        return new UpdateResponse(MESSAGE, "서버에 문제가 발생하였습니다. 죄송합니다.");
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

    private Optional<TelegramException> causedByTelegramException(Throwable e) {
        if(e instanceof TelegramException)
            return Optional.of((TelegramException)e);
        final Throwable cause = e.getCause();
        if(cause !=null){
            causedByTelegramException(cause);
        }
        return Optional.empty();
    }

    private void replaceLastUpdateId(Long updateId) {
        if (updateId > LAST_UPDATE_ID)
            LAST_UPDATE_ID = updateId;
    }
}
