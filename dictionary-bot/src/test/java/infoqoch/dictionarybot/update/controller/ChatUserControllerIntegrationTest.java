package infoqoch.dictionarybot.update.controller;

import infoqoch.dictionarybot.DictionaryUpdateRunner;
import infoqoch.dictionarybot.integration.dictionarybot.FakeSendRequestEventListener;
import infoqoch.dictionarybot.mock.data.MockUpdate;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.model.user.ChatUserRepository;
import infoqoch.dictionarybot.run.FakeTelegramBot;
import infoqoch.dictionarybot.run.FakeTelegramUpdate;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.log.repository.UpdateLogJpaRepository;
import infoqoch.telegrambot.bot.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// jpa의 트랜잭션 동작 유무를 확인해야 하기 때문에 불가피하게 통합테스트를 진행
// fake는 SendRequestEventListener와 FakeTelegramUpdate 임. 나머지는 빈으로 조립.
@ActiveProfiles({"test", "fake_send_listener"})
@SpringBootTest
@Transactional
class ChatUserControllerIntegrationTest {
    @Autowired UpdateLogJpaRepository repository;
    @Autowired UpdateDispatcher updateDispatcher;

    // fake. 가상의 update를 보냄.
    FakeTelegramUpdate telegramUpdate;

    // fake_send_listener가 동작할 때 우선 순위로 빈이 등록됨
    @Autowired FakeSendRequestEventListener fakeSendRequestEventListener;

    // 타겟 객체
    DictionaryUpdateRunner dictionaryUpdateRunner;

    @Autowired
    ChatUserRepository chatUserRepository;

    @BeforeEach
    void setUp(){
        fakeSendRequestEventListener.setCalled(false); // 빈을 재생성하지 않고 기본 값인 false로 롤백한다.
        dictionaryUpdateRunner = new DictionaryUpdateRunner(generateFakeBot(), updateDispatcher, repository);
    }

    private TelegramBot generateFakeBot() {
        telegramUpdate = new FakeTelegramUpdate();
        return new FakeTelegramBot(telegramUpdate, null);
    }


    // UpdateRequestMethod와 관련한 resolver는 빈으로 등록하지 않고 이로 인하여 트랜잭션 흐름이 생길 수 없음.
    // 결과적으로 controller에서 repository를 호출하여 save 하는 형태로 개발하였음. 정상 동작함을 확인
    @Test
    @DisplayName("share_mine의 수정")
    void share_mine_change() {
        // given
        final Optional<ChatUser> beforeChatUser = chatUserRepository.findByChatId(123l);
        assert beforeChatUser.isEmpty();

        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/share_mine_N", 123l));

        // when
        dictionaryUpdateRunner.run();

        // then
        final Optional<ChatUser> afterChatUser = chatUserRepository.findByChatId(123l);
        assertThat(afterChatUser).isPresent();
        assertThat(afterChatUser.get().isShareMine()).isFalse();
    }

    @Autowired
    EntityManager em;

    /*
    *
    * TODO
    * no session 문제가 발생하였다. 정확하게는 requestParamResolver에서 chatId를 기준으로 repository에 존재하면 Optional#get을 리턴하고, 그렇지 않으면 JpaRepository#save 객체를 리턴한다.
    * save를 한 객체는 controller로 넘어가는데, 이때 영속성이 유지가 된다. 그러니까 엔티티그래프를 통해 다른 객체를 호출할 수 있다.
    * find한 객체는 controller로 넘어가면 영속성이 끝나버리고 no session 에러가 발생한다.
    * 이 두 개의 차이는 무엇일까?
    * 일단은 front controller가 발생하는 곳에 @Transactional를 선언하여 해소할 수 있었다. 차후 이러한 문제가 발생하는 이유를 분명하게 이해하기를 바란다.
    *
    */
    @Test
    @DisplayName("chatuser의 1차 캐시의 유지")
    void share_mine_change_in_session() {
        // given
        em.persist(new ChatUser(123l, "kim"));
        em.flush();
        em.clear();;

        final Optional<ChatUser> beforeChatUser = chatUserRepository.findByChatId(123l);
        assert beforeChatUser.isPresent();

        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/share_mine_N", 123l));

        // when
        dictionaryUpdateRunner.run();

        // then
        final Optional<ChatUser> afterChatUser = chatUserRepository.findByChatId(123l);
        assertThat(afterChatUser).isPresent();
        assertThat(afterChatUser.get().isShareMine()).isFalse();
    }
}