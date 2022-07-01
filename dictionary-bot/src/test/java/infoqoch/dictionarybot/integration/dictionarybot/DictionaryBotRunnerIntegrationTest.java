package infoqoch.dictionarybot.integration.dictionarybot;

import infoqoch.dictionarybot.DictionaryBotRunner;
import infoqoch.dictionarybot.bot.FakeTelegramBot;
import infoqoch.dictionarybot.bot.FakeTelegramSend;
import infoqoch.dictionarybot.bot.FakeTelegramUpdate;
import infoqoch.dictionarybot.mock.data.MockSendResponse;
import infoqoch.dictionarybot.send.SendDispatcher;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.mock.data.MockUpdate;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

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

    // TODO. 통합테스트에 대한 고민이 있음. 일단은 실제 운영에 들어가면 DB에서 데이터를 읽을 것이다. 이를 어떻게 대역으로 만들지 고민임.
    @Test
    void lookupByWord_no_result(){
        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/w wfjwef98wfj9w8efjew98fjwe98fj", 123l)); // telegram에서 받은 값
        telegramSend.setMockMessageResponseJson(MockSendResponse.sendMessage("/w wfjwef98wfj9w8efjew98fjwe98fj", 123l)); // response 이후 telegram에서 받은 응답값
        dictionaryBotRunner.run();

        final SendMessageRequest sendMessageRequest = telegramSend.getSendMessageRequest();
        assertThat(sendMessageRequest.getText()).isEqualTo("검색결과를 찾을 수 없습니다\\.");
    }

}