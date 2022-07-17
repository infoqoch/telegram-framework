package infoqoch.dictionarybot.model.dictionary.repository;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import infoqoch.dictionarybot.model.user.ChatUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static infoqoch.dictionarybot.model.dictionary.QDictionary.dictionary;
import static infoqoch.dictionarybot.model.dictionary.repository.LookupRepository.FindBy.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LookupRepositoryTest {
    @Autowired EntityManager em;

    JPAQueryFactory queryFactory;

    @Autowired
    LookupRepository repositoryV2;

    @BeforeEach
    void setUp() {
        queryFactory = new JPAQueryFactory(em);
    }

    ChatUser chatUser = null;


    /*
    * 하나의 타입(word)만을 검색
    * limit offset 위주로 테스트
    */
    void word_setUp() {
        chatUser_setUp();

        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("hot summer").build())); // endsWith
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("winter").build())); // etc

        final TypedQuery<Dictionary> query = em.createQuery("select d from Dictionary d where d.content.word = :value", Dictionary.class);
        query.setParameter("value", "summer");
        final List<Dictionary> resultList = query.getResultList();
        assert resultList.size()==1;
    }

    private void chatUser_setUp() {
        chatUser = new ChatUser(ThreadLocalRandom.current().nextLong(), "kim");
        em.persist(chatUser);
    }

    @Test
    void lookup_just_limit() {
        // given
        chatUser_setUp();
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("winter").build())); // etc
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith

        // when
        final List<Dictionary> result = repositoryV2.lookup(4, 0, "summer", chatUser, WORD);

        // then
        assertThat(result.stream().map(s -> s.getContent().getWord())).containsExactly("summer", "summer vacation", "I like summer.");
    }

    @Test
    void lookup_limit_lt() {
        // given
        chatUser_setUp();
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("winter").build())); // etc
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith

        // when
        final List<Dictionary> result = repositoryV2.lookup(1, 0, "summer", chatUser, WORD);

        // then
        assertThat(result.stream().map(s -> s.getContent().getWord())).containsExactly("summer");
    }

    @Test
    void lookup_limit_gt() {
        // given
        chatUser_setUp();
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("winter").build())); // etc
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith

        // when
        final List<Dictionary> result = repositoryV2.lookup(10, 0, "summer", chatUser, WORD);

        // then
        assertThat(result.stream().map(s -> s.getContent().getWord())).containsExactly("summer", "summer vacation", "I like summer.");
    }

    @Test
    void lookup_offset() {
        // given
        chatUser_setUp();
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("winter").build())); // etc
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith

        // when
        final List<Dictionary> result = repositoryV2.lookup(1, 1, "summer", chatUser, WORD);

        // then
        assertThat(result.stream().map(s -> s.getContent().getWord())).containsExactly("summer vacation");
    }

    @Test
    void lookup_offset_gt() {
        // given
        chatUser_setUp();
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("winter").build())); // etc
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith

        // when
        final List<Dictionary> result = repositoryV2.lookup(10, 10, "summer", chatUser, WORD);

        // then
        assertThat(result).size().isEqualTo(0);
    }

    /*
    * FindBy에 따른 테스트
    */

    @Test
    void find_by_nothing() {
        // given
        chatUser_setUp();
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("winter").build())); // etc
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith

        // when
        final List<Dictionary> result = repositoryV2.lookup(10, 0, "summer", chatUser, SENTENCE);

        // then
        assertThat(result).size().isEqualTo(0);
    }

    @Disabled("FindBy가 존재하나, eq-startsWith-contains가 우선하여 이에 따라 정렬됨")
    @Test
    void find_by_mixed_data() {
        // given
        chatUser_setUp();
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().definition("winter").build())); // etc
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().sentence("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().definition("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith

        // when
        final List<Dictionary> result = repositoryV2.lookup(10, 0, "summer", chatUser, SENTENCE);

        // then
        assertThat(result.stream().map(s -> s.getContent().getSentence())).containsExactly("I like summer.");
    }

    @DisplayName("FindBy가 복수")
    @Test
    void multiple_find_by() {
        // given
        chatUser_setUp();
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().definition("winter").build())); // etc
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().sentence("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().definition("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith

        // when
        final List<Dictionary> result = repositoryV2.lookup(10, 0, "summer", chatUser, WORD, DEFINITION, SENTENCE);

        // then
        assertThat(result.get(0).getContent().getDefinition()).isEqualTo("summer");
        assertThat(result.get(1).getContent().getWord()).isEqualTo("summer vacation");
        assertThat(result.get(2).getContent().getSentence()).isEqualTo("I like summer.");
    }

    @DisplayName("findBy 순서 까지는 보장하지 않음.")
    @Test
    void multiple_find_by_order_by_like() {
        // given
        chatUser_setUp();
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().definition("summer").build()));
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().sentence("summer").build()));
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer").build()));
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("I love summer!").build()));

        // when
        final List<Dictionary> result = repositoryV2.lookup(10, 0, "summer", chatUser, WORD, DEFINITION, SENTENCE);

        // then
        assertThat(result).size().isEqualTo(4);
        assertThat(result.get(3).getContent().getWord()).isEqualTo("I love summer!");
    }


    /*
    *
    * booleanExpression 의 복사와 관련한 테스트
    *
    */

    // @Disabled
    @Test
    void booleanExpression_return_this(){
        final BooleanExpression eq = dictionary.content.word.eq("summer");
        System.out.println("origin = " + eq);
        for(int i=0; i<3; i++){
            final BooleanExpression target = eq.not();
            System.out.println(i + " : " + target);
        }
        System.out.println("origin = " + eq);
//        origin = dictionary.content.word = summer
//        0 : !(dictionary.content.word = summer)
//        1 : !(dictionary.content.word = summer)
//        2 : !(dictionary.content.word = summer)
    }

    // @Disabled
    @Test
    void booleanExpression_copy() {
        final BooleanExpression eq = dictionary.content.word.eq("summer");
        final BooleanExpression ne = eq.not();
        final BooleanExpression nne = ne.not();
        System.out.println("eq = " + eq); // positive
        System.out.println("ne = " + ne); // negative
        System.out.println("nne = " + nne); // positive
    }
}
