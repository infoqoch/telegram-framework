package infoqoch.telegram.framework.update.controller;

import infoqoch.dictionarybot.main.UpdateRunner;
import infoqoch.dictionarybot.mock.FakeSendRequestEventListener;
import infoqoch.dictionarybot.mock.data.MockUpdate;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.model.user.ChatUserRepository;
import infoqoch.dictionarybot.mock.bot.FakeTelegramBot;
import infoqoch.dictionarybot.mock.bot.FakeTelegramUpdate;
import infoqoch.dictionarybot.system.properties.DictionaryProperties;
import infoqoch.telegram.framework.update.util.TelegramProperties;
import infoqoch.telegram.framework.update.UpdateDispatcher;
import infoqoch.dictionarybot.log.repository.UpdateLogJpaRepository;
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

import static infoqoch.dictionarybot.model.user.ChatUser.Role.ADMIN;
import static infoqoch.dictionarybot.model.user.ChatUser.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;

// 텔레그램에서 ChatUser와 관련한 메시지를 받는다. ChatUser의 데이터가 정상 수정되는지를 확인한다.
// 대역을 사용한다. SendRequestEventListener, FakeTelegramUpdate.
// 그 외는 빈을 사용하여 조립한다.
@ActiveProfiles({"test", "fake_send_listener"})
@SpringBootTest
@Transactional
class ChatUserControllerIntegrationTest {
    @Autowired UpdateLogJpaRepository repository;
    @Autowired
    UpdateDispatcher updateDispatcher;
    @Autowired
    TelegramProperties telegramProperties;
    @Autowired DictionaryProperties dictionaryProperties;

    // fake. 가상의 update를 보냄.
    FakeTelegramUpdate telegramUpdate;

    // fake_send_listener가 동작할 때 우선 순위로 빈이 등록됨
    @Autowired FakeSendRequestEventListener fakeSendRequestEventListener;

    // 타겟 객체
    UpdateRunner updateRunner;

    @Autowired
    ChatUserRepository chatUserRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    void setUp(){
        // 빈을 반복하여 사용하므로, 초기화한다.
        fakeSendRequestEventListener.setCalled(false);
        updateRunner = new UpdateRunner(generateFakeBot(), updateDispatcher, repository);
    }

    private TelegramBot generateFakeBot() {
        telegramUpdate = new FakeTelegramUpdate();
        return new FakeTelegramBot(telegramUpdate, null);
    }

    @Test
    @DisplayName("어드민으로 프로모션 성공")
    void success_promotion(){
        // given
        assert dictionaryProperties.user().promotionToAdmin()!=null;
        System.out.println("telegramProperties.user().promotionToAdmin() = " + dictionaryProperties.user().promotionToAdmin());

        final Optional<ChatUser> beforeChatUser = chatUserRepository.findByChatId(123l);
        assert beforeChatUser.isEmpty();

        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/promotion_"+dictionaryProperties.user().promotionToAdmin(), 123l));

        // when
        updateRunner.run();

        // then
        final Optional<ChatUser> afterChatUser = chatUserRepository.findByChatId(123l);
        assertThat(afterChatUser).isPresent();
        assertThat(afterChatUser.get().getRole()).isEqualTo(ADMIN);
    }
    
    @Test
    @DisplayName("어드민으로 프로모션 성공, 언더스코어")
    void success_promotion_underscore(){
        // given
        final String expectCode = dictionaryProperties.user().promotionToAdmin().replaceAll(" ", "_");
        assert expectCode !=null;
        System.out.println("telegramProperties.user().promotionToAdmin() = " + expectCode);

        final Optional<ChatUser> beforeChatUser = chatUserRepository.findByChatId(123l);
        assert beforeChatUser.isEmpty();

        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/promotion_"+ expectCode, 123l));

        // when
        updateRunner.run();

        // then
        final Optional<ChatUser> afterChatUser = chatUserRepository.findByChatId(123l);
        assertThat(afterChatUser).isPresent();
        assertThat(afterChatUser.get().getRole()).isEqualTo(ADMIN);
    }

    @Test
    @DisplayName("어드민으로 프로모션 실패")
    void fail_promotion_underscore(){
        // given
        final Optional<ChatUser> beforeChatUser = chatUserRepository.findByChatId(123l);
        assert beforeChatUser.isEmpty();

        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/promotion_wrong_code!!", 123l));

        // when
        updateRunner.run();

        // then
        final Optional<ChatUser> afterChatUser = chatUserRepository.findByChatId(123l);
        assertThat(afterChatUser).isPresent();
        assertThat(afterChatUser.get().getRole()).isEqualTo(USER);
    }

    // UpdateRequestParam 빈에서 찾은 객체가 Controller까지 영속성 컨텍스트까지 유지함을 확인한다.
    @Test
    @DisplayName("UpdateRequestParam에서 저장(persist)한 ChatUser가 1차캐시에 있으며 더티체킹을 할 수 있다.")
    void share_mine_change_in_session() {
        // given
        em.persist(new ChatUser(123l, "kim"));
        em.flush();
        em.clear();;

        final Optional<ChatUser> beforeChatUser = chatUserRepository.findByChatId(123l);
        assert beforeChatUser.isPresent();

        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/share_mine_N", 123l));

        // when
        updateRunner.run();

        // then
        final Optional<ChatUser> afterChatUser = chatUserRepository.findByChatId(123l);
        assertThat(afterChatUser).isPresent();
        assertThat(afterChatUser.get().isShareMine()).isFalse();
    }

    // TODO 추가적인 학습이 필요 : save의 경우 Transactional을 선언하지 않아도 영속성 컨텍스트에 유지되는데, findBy는 끊어진다. 결과적으로 Transactional을 선언하며 해소하였음. 이 이유는 차후 학습 필요.
    @Test
    @DisplayName("UpdateRequestParam으로 찾은(findBy) ChatUser가 1차캐시에 있으며 더티체킹을 할 수 있다.")
    void share_mine_change() {
        // given
        final Optional<ChatUser> beforeChatUser = chatUserRepository.findByChatId(123l);
        assert beforeChatUser.isEmpty();

        telegramUpdate.setMock(MockUpdate.responseWithSingleChat("/share_mine_N", 123l));

        // when
        updateRunner.run();

        // then
        final Optional<ChatUser> afterChatUser = chatUserRepository.findByChatId(123l);
        assertThat(afterChatUser).isPresent();
        assertThat(afterChatUser.get().isShareMine()).isFalse();
    }
}