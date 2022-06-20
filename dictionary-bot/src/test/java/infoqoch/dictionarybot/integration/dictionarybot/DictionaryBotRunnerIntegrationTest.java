package infoqoch.dictionarybot.integration.dictionarybot;

import infoqoch.dictionarybot.bot.DictionaryBotRunner;
import infoqoch.dictionarybot.bot.FakeTelegramBot;
import infoqoch.dictionarybot.bot.FakeTelegramSend;
import infoqoch.dictionarybot.bot.FakeTelegramUpdate;
import infoqoch.dictionarybot.send.MockSendResponseGenerate;
import infoqoch.dictionarybot.send.SendDispatcher;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.request.body.MockUpdateGenerate;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// 외부 api인 telegram-bot 만 대역으로 사용함.
// 그 외는 스프링 통합 테스트를 진행함.
@SpringBootTest
class DictionaryBotRunnerIntegrationTest {

    // 컨텍스트 객체
    @Autowired
    private UpdateDispatcher updateDispatcher;

    // fake 객체, 외부 api는 가짜로 응답하고 spy로 내부 데이터를 확인한다.
    TelegramBot bot;
    FakeTelegramSend telegramSend;
    FakeTelegramUpdate telegramUpdate;

    private SendDispatcher sendDispatcher;

    // 타겟 객체
    DictionaryBotRunner dictionaryBotRunner;

    @BeforeEach
    void setUp(){
        generateFakeBot();
        sendDispatcher = new SendDispatcher(bot);
        dictionaryBotRunner = new DictionaryBotRunner(bot, updateDispatcher, sendDispatcher);
    }

    private void generateFakeBot() {
        telegramSend = new FakeTelegramSend();
        telegramUpdate = new FakeTelegramUpdate();
        bot = new FakeTelegramBot(telegramUpdate, telegramSend);
    }

    @Test
    void test(){
        telegramUpdate.setMock(MockUpdateGenerate.responseWithSingleChat("/w hi", 123l)); // telegram에서 받은 값
        telegramSend.setMockMessageResponseJson(MockSendResponseGenerate.sendMessage("/w hi", 123l)); // response 이후 telegram에서 받은 응답값
        dictionaryBotRunner.run();

        final SendMessageRequest sendMessageRequest = telegramSend.getSendMessageRequest();
        System.out.println("sendMessageRequest = " + sendMessageRequest);
    }


}