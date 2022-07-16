 package infoqoch.dictionarybot.model.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChatUserRepositoryTest {
    @Autowired EntityManager em;
    @Autowired ChatUserRepository chatUserRepository;

    @Test
    void change_role(){
        // given
        ChatUser chatUser = new ChatUser(123l, "kim");
        chatUser.changeRole(ChatUser.Role.ADMIN);
        em.persist(chatUser);
        em.persist(new ChatUser(1234l, "lee"));
        em.flush();
        em.clear();

        // when
        final List<ChatUser> result = chatUserRepository.findByRole(ChatUser.Role.ADMIN);

        // then
        assertThat(result).size().isEqualTo(1);
        assertThat(result.get(0).getNickName()).isEqualTo("kim");
    }

}