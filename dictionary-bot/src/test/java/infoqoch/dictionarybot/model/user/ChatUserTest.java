package infoqoch.dictionarybot.model.user;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ChatUserTest {

    @Autowired
    EntityManager em;

    @Autowired
    ChatUserRepository chatUserRepository;

    @Autowired
    DictionaryRepository dictionaryRepository;

    @Test
    void join_chat_dictionary(){
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

    @Test
    void setup_booleans(){
        // given
        final ChatUser givenUser = ChatUser.createUser(123l, "김갑순");
        em.persist(givenUser);
        em.flush();
        em.clear();

        final ChatUser findUser = em.find(ChatUser.class, givenUser.getNo());
        assert findUser.isOpenDataPublic();
        assert findUser.isLookupPublicData();

        // when
        findUser.setOpenDataPublic(false);
        findUser.setLookupPublicData(false);

        // then
        final ChatUser result = em.find(ChatUser.class, givenUser.getNo());
        assertThat(result.isOpenDataPublic()).isFalse();
        assertThat(result.isLookupPublicData()).isFalse();
    }
}