package infoqoch.dictionarybot.integration.dictionarybot;

import infoqoch.dictionarybot.DictionaryUpdateRunner;
import infoqoch.dictionarybot.run.FakeTelegramBot;
import infoqoch.dictionarybot.run.FakeTelegramUpdate;
import infoqoch.dictionarybot.mock.data.MockUpdate;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.log.UpdateLog;
import infoqoch.dictionarybot.update.log.repository.MemoryUpdateLogRepository;
import infoqoch.dictionarybot.update.request.UpdateRequestCommand;
import infoqoch.telegrambot.bot.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// 외부 API를 대역으로 사용함. // FakeTelegramUpdate
// repository는 MemoryUpdateLogRepository를 사용함.
@SpringBootTest
class DictionaryUpdateRunnerIntegrationTest {

    // 컨텍스트 객체
    @Autowired
    private UpdateDispatcher updateDispatcher;

    // fake 객체, 외부 api는 가짜로 응답하고 spy로 내부 데이터를 확인한다.
    TelegramBot bot;
    FakeTelegramUpdate telegramUpdate;
    MemoryUpdateLogRepository repository;

    // 타겟 객체
    DictionaryUpdateRunner dictionaryUpdateRunner;

    @BeforeEach
    void setUp(){
        generateFakeBot();
        dictionaryUpdateRunner = new DictionaryUpdateRunner(bot, updateDispatcher, repository);
    }

    private void generateFakeBot() {
        repository = new MemoryUpdateLogRepository();
        telegramUpdate = new FakeTelegramUpdate();
        bot = new FakeTelegramBot(telegramUpdate, null);
    }

    // TODO
    // 통합테스트가 좀 더 촘촘하게 할 필요가 있어보임. 비록 DB가 아닌 Java의 자료구조를 repository로 하더라도. 이 부분은 리팩터링 및 테스트 코드 추가 필요할 듯.
    @Test
    void lookupByWord_no_result(){
        // given
        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/w wfjwef98wfj9w8efjew98fjwe98fj", 123l));

        // when
        dictionaryUpdateRunner.run();

        // then
        final List<UpdateLog> logs = repository.findAll();

        assertThat(logs).size().isEqualTo(1);
        assertThat(logs.get(0).getUpdateCommand()).isEqualTo(UpdateRequestCommand.LOOKUP_WORD);
        assertThat(logs.get(0).getUpdateValue()).isEqualTo("wfjwef98wfj9w8efjew98fjwe98fj");
        assertThat(logs.get(0).getSendMessage()).isEqualTo("검색결과를 찾을 수 없습니다\\.");
    }

}