package infoqoch.dictionarybot.model.user;

import infoqoch.mock.repository.QuerydslConfig;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static infoqoch.dictionarybot.model.user.ChatUser.Role.ADMIN;
import static infoqoch.dictionarybot.model.user.ChatUser.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class ChatUserRepositoryJpaTest {

    @Autowired
    EntityManager em;

    @Autowired
    ChatUserRepository chatUserRepository;

    @Autowired
    DictionaryRepository dictionaryRepository;

    @DisplayName("회원이 생성되고 사전이 잘 등록된다")
    @Test
    void join_chat_dictionary() {
        // given
        final ChatUser chatUser = ChatUser.createUser(123l, "김갑순");
        chatUserRepository.save(chatUser);

        final Dictionary dictionary = Dictionary.builder()
                .chatUser(chatUser)
                .content(DictionaryContent.builder().word("hot summer").build())
                .build();
        dictionaryRepository.save(dictionary);

        em.flush();
        em.clear();

        // when
        final Optional<ChatUser> optionalChatUser = chatUserRepository.findByChatId(123l);
        assert optionalChatUser.isPresent();
        final List<Dictionary> dictionaries = optionalChatUser.get().getDictionaries();

        // then
        assertThat(dictionaries).size().isEqualTo(1);
        assertThat(dictionaries.get(0).getContent().getWord()).isEqualTo("hot summer");
    }

    @DisplayName("회원의 상태값이 잘 변경된다")
    @Test
    void setup_booleans() {
        // given
        final ChatUser givenUser = ChatUser.createUser(123l, "김갑순");
        em.persist(givenUser);
        em.flush();
        em.clear();

        final ChatUser findUser = em.find(ChatUser.class, givenUser.getNo());
        assert findUser.isShareMine();
        assert findUser.isLookupAllUsers();
        assert findUser.isHourlyAlarm();
        assert findUser.getRole().equals(USER);

        // when
        findUser.setShareMine(false);
        findUser.setLookupAllUsers(false);
        findUser.setHourlyAlarm(false);
        findUser.changeRole(ADMIN);

        // then
        final ChatUser result = em.find(ChatUser.class, givenUser.getNo());
        assertThat(result.isShareMine()).isFalse();
        assertThat(result.isLookupAllUsers()).isFalse();
        assertThat(result.isHourlyAlarm()).isFalse();
        assertThat(result.getRole()).isEqualTo(ADMIN);
    }
}