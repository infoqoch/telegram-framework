package infoqoch.dictionarybot.model.dictionary.repository;


import com.querydsl.core.types.dsl.BooleanExpression;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import infoqoch.dictionarybot.model.user.ChatUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static infoqoch.dictionarybot.model.dictionary.QDictionary.dictionary;
import static infoqoch.dictionarybot.model.dictionary.repository.LookupRepository.FindBy.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LookupRepositoryJpaTest {

    @Autowired
    EntityManager em;

    @Autowired LookupRepository repository;

    ChatUser chatUser = null;

    @BeforeEach
    void setUp() {
        chatUser = new ChatUser(ThreadLocalRandom.current().nextLong(), "kim");
        em.persist(chatUser);
    }

    @Test
    void lookup_just_limit() {
        // given
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("winter").build())); // etc
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith

        // when
        final List<Dictionary> result = repository.lookup(4, 0, "summer", chatUser, WORD);

        // then
        assertThat(result.stream().map(s -> s.getContent().getWord())).containsExactly("summer", "summer vacation", "I like summer.");
    }

    @Test
    void lookup_limit_lt() {
        // given
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("winter").build())); // etc
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith

        // when
        final List<Dictionary> result = repository.lookup(1, 0, "summer", chatUser, WORD);

        // then
        assertThat(result.stream().map(s -> s.getContent().getWord())).containsExactly("summer");
    }

    @Test
    void lookup_limit_gt() {
        // given
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("winter").build())); // etc
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith

        // when
        final List<Dictionary> result = repository.lookup(10, 0, "summer", chatUser, WORD);

        // then
        assertThat(result.stream().map(s -> s.getContent().getWord())).containsExactly("summer", "summer vacation", "I like summer.");
    }

    @Test
    void lookup_offset() {
        // given
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("winter").build())); // etc
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith

        // when
        final List<Dictionary> result = repository.lookup(1, 1, "summer", chatUser, WORD);

        // then
        assertThat(result.stream().map(s -> s.getContent().getWord())).containsExactly("summer vacation");
    }

    @Test
    void lookup_offset_gt() {
        // given
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("winter").build())); // etc
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith

        // when
        final List<Dictionary> result = repository.lookup(10, 10, "summer", chatUser, WORD);

        // then
        assertThat(result).size().isEqualTo(0);
    }

    /*
    * FindBy에 따른 테스트
    */

    @Test
    void find_by_column_nothing() {
        // given
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("winter").build())); // etc
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith

        // when
        final List<Dictionary> result = repository.lookup(10, 0, "summer", chatUser, SENTENCE);

        // then
        assertThat(result).size().isEqualTo(0);
    }

    @DisplayName("해당 검색어를 다수의 칼럼에서 찾을 수 있으나 FindBy가 단 하나의 칼럼에서 찾기를 바람")
    @Test
    void find_by_mixed_data() {
        // given
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().definition("winter").build())); // etc
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().sentence("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().definition("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith

        // when
        final List<Dictionary> result = repository.lookup(10, 0, "summer", chatUser, SENTENCE);

        // then
        assertThat(result.stream().map(s -> s.getContent().getSentence())).containsExactly("I like summer.");
    }

    @DisplayName("해당 검색어가 다수의 칼럼에서 검색할 수 있고, FindBy가 다수의 칼럼에서 찾기를 바람. 다만 그것의 위치(where like)에 따라 순서가 보장되는 상황")
    @Test
    void multiple_find_by() {
        
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().definition("winter").build())); // etc
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().sentence("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().definition("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith

        // when
        final List<Dictionary> result = repository.lookup(10, 0, "summer", chatUser, WORD, DEFINITION, SENTENCE);

        // then
        assertThat(result.get(0).getContent().getDefinition()).isEqualTo("summer");
        assertThat(result.get(1).getContent().getWord()).isEqualTo("summer vacation");
        assertThat(result.get(2).getContent().getSentence()).isEqualTo("I like summer.");
    }

    @DisplayName("해당 검색어가 다수의 칼럼에서 검색할 수 있고, FindBy가 다수의 칼럼에서 찾기를 바람. 다만 그것의 위치(where like)에 따라 순서가 보장됨. 순서가 보장되는 확실한 것을 검색")
    @Test
    void multiple_find_by_order_by_like() {
        
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().definition("summer").build()));
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().sentence("summer").build()));
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer").build()));
        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("I love summer!").build()));

        // when
        final List<Dictionary> result = repository.lookup(10, 3, "summer", chatUser, WORD, DEFINITION, SENTENCE);

        // then
        assertThat(result).size().isEqualTo(1);
        assertThat(result.get(0).getContent().getWord()).isEqualTo("I love summer!");
    }

    /*
    *
    * booleanExpression 의 불변성에 관련한 테스트
    *
    */

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
