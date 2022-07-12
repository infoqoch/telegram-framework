package infoqoch.dictionarybot.model.dictionary.repository;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import infoqoch.dictionarybot.model.user.ChatUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DictionaryQueryRepositoryWithChatUserTest {

    @Autowired
    EntityManager em;

    @Autowired

    DictionaryQueryRepository dictionaryQueryRepository;

    @Test
    void lookup_public_data(){
        // given
        ChatUser narcissus = new ChatUser(123L, "narcissus");
        narcissus.setOpenDataPublic(true);
        narcissus.setLookupPublicData(false);
        em.persist(narcissus);
        em.persist(new Dictionary(null, narcissus, null, DictionaryContent.builder().word("apple").sentence("누가 세상에서 가장 단어정리를 잘할까? 바로 나야^^ 내 것만 보기에도 시간이 없어.").build()));

        ChatUser opened = new ChatUser(345L, "opened");
        opened.setOpenDataPublic(true);
        opened.setLookupPublicData(true);
        em.persist(opened);
        em.persist(new Dictionary(null, opened, null, DictionaryContent.builder().word("apple").sentence("다른 사람과 공유하고 싶어!").build()));

        em.flush();
        em.clear();

        // when
        System.out.println("=======================시작======================");
        final List<Dictionary> findByNarcissus = dictionaryQueryRepository.findByContentWord(narcissus, "apple");
        assertThat(findByNarcissus).size().isEqualTo(1); // setLookupPublicData(false) // 자기 자신 것만 찾으므로 사이즈는 1개이다.

        final List<Dictionary> findByOpened = dictionaryQueryRepository.findByContentWord(opened, "apple");
        assertThat(findByOpened).size().isEqualTo(2); // 다른 회원의 데이터도 찾으므로 2개를 찾는다.
    }

    @Test
    void open_data_public(){
        // given
        ChatUser closed = new ChatUser(123L, "closed");
        closed.setOpenDataPublic(false);
        closed.setLookupPublicData(true);
        em.persist(closed);
        em.persist(new Dictionary(null, closed, null, DictionaryContent.builder().word("apple").sentence("나만 알고 싶은 단어^^ 남들에게는 안보여주지롱~").build()));

        ChatUser opened = new ChatUser(345L, "opened");
        opened.setOpenDataPublic(true);
        opened.setLookupPublicData(true);
        em.persist(opened);
        em.persist(new Dictionary(null, opened, null, DictionaryContent.builder().word("apple").sentence("다른 사람과 공유하고 싶어!").build()));

        em.flush();
        em.clear();

        // when
        System.out.println("=======================시작======================");
        final List<Dictionary> findByClosed = dictionaryQueryRepository.findByContentWord(closed, "apple");
        assertThat(findByClosed).size().isEqualTo(2); // 모든 데이터에 대하여 검색함.

        final List<Dictionary> findByOpened = dictionaryQueryRepository.findByContentWord(opened, "apple");
        assertThat(findByOpened).size().isEqualTo(1); // closed.setOpenDataPublic(false); // opened는 closed의 데이터를 검색할 수 없음.
    }
}