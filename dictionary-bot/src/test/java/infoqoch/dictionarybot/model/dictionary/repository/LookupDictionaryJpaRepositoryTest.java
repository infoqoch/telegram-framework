package infoqoch.dictionarybot.model.dictionary.repository;


import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test_jpa")
@SpringBootTest
@Transactional
class LookupDictionaryJpaRepositoryTest {
    @Autowired EntityManager em;

    @Autowired DictionaryJpaRepository repository;

    void word_setUp() {
        em.persist(new Dictionary(null, null, DictionaryContent.builder().word("summer").build())); // exact match
        em.persist(new Dictionary(null, null, DictionaryContent.builder().word("summer vacation").build())); // startsWith
        em.persist(new Dictionary(null, null, DictionaryContent.builder().word("hot summer").build())); // endsWith
        em.persist(new Dictionary(null, null, DictionaryContent.builder().word("I like summer.").build())); // contains
        em.persist(new Dictionary(null, null, DictionaryContent.builder().word("winter").build())); // etc

        final TypedQuery<Dictionary> query = em.createQuery("select d from Dictionary d where d.content.word = :value", Dictionary.class);
        query.setParameter("value", "summer");
        final List<Dictionary> resultList = query.getResultList();
        assert resultList.size()==1;
    }

    @Test
    void findByWord() {
        word_setUp();

        final List<Dictionary> exact = repository.findByContentWord("summer");
        assertThat(exact).size().isEqualTo(1);
        assertThat(exact.get(0).getContent().getWord()).isEqualTo("summer");

        final List<Dictionary> start = repository.findByContentWordStartsWith("summer");
        assertThat(start).size().isEqualTo(2);
        assertThat(start.stream().map(d -> d.getContent().getWord())).contains("summer", "summer vacation");

        final List<Dictionary> end = repository.findByContentWordEndsWith("summer");
        assertThat(end).size().isEqualTo(2);
        assertThat(end.stream().map(d -> d.getContent().getWord())).contains("summer", "hot summer");


        final List<Dictionary> contains = repository.findByContentWordContains("summer");
        assertThat(contains).size().isEqualTo(4);
        assertThat(contains.stream().map(d -> d.getContent().getWord())).contains("summer", "summer vacation", "hot summer", "I like summer.");
    }

    void definition_setUp() {
        em.persist(new Dictionary(null, null, DictionaryContent.builder().definition("summer").build())); // exact match
        em.persist(new Dictionary(null, null, DictionaryContent.builder().definition("summer vacation").build())); // startsWith
        em.persist(new Dictionary(null, null, DictionaryContent.builder().definition("hot summer").build())); // endsWith
        em.persist(new Dictionary(null, null, DictionaryContent.builder().definition("I like summer.").build())); // contains
        em.persist(new Dictionary(null, null, DictionaryContent.builder().definition("winter").build())); // etc
    }

    @Test
    void findByDefinition() {
        definition_setUp();

        final List<Dictionary> exact = repository.findByContentDefinition("summer");
        assertThat(exact).size().isEqualTo(1);
        assertThat(exact.get(0).getContent().getDefinition()).isEqualTo("summer");

        final List<Dictionary> start = repository.findByContentDefinitionStartsWith("summer");
        assertThat(start).size().isEqualTo(2);
        assertThat(start.stream().map(d -> d.getContent().getDefinition())).contains("summer", "summer vacation");

        final List<Dictionary> end = repository.findByContentDefinitionEndsWith("summer");
        assertThat(end).size().isEqualTo(2);
        assertThat(end.stream().map(d -> d.getContent().getDefinition())).contains("summer", "hot summer");

        final List<Dictionary> contains = repository.findByContentDefinitionContains("summer");
        assertThat(contains).size().isEqualTo(4);
        assertThat(contains.stream().map(d -> d.getContent().getDefinition())).contains("summer", "summer vacation", "hot summer", "I like summer.");
    }


    void sentence_setUp() {
        em.persist(new Dictionary(null, null, DictionaryContent.builder().sentence("summer").build())); // exact match
        em.persist(new Dictionary(null, null, DictionaryContent.builder().sentence("summer vacation").build())); // startsWith
        em.persist(new Dictionary(null, null, DictionaryContent.builder().sentence("hot summer").build())); // endsWith
        em.persist(new Dictionary(null, null, DictionaryContent.builder().sentence("I like summer.").build())); // contains
        em.persist(new Dictionary(null, null, DictionaryContent.builder().sentence("winter").build())); // etc
    }

    @Test
    void findBySentence() {
        sentence_setUp();

        final List<Dictionary> exact = repository.findByContentSentence("summer");
        assertThat(exact).size().isEqualTo(1);
        assertThat(exact.get(0).getContent().getSentence()).isEqualTo("summer");

        final List<Dictionary> start = repository.findByContentSentenceStartsWith("summer");
        assertThat(start).size().isEqualTo(2);
        assertThat(start.stream().map(d -> d.getContent().getSentence())).contains("summer", "summer vacation");

        final List<Dictionary> end = repository.findByContentSentenceEndsWith("summer");
        assertThat(end).size().isEqualTo(2);
        assertThat(end.stream().map(d -> d.getContent().getSentence())).contains("summer", "hot summer");

        final List<Dictionary> contains = repository.findByContentSentenceContains("summer");
        assertThat(contains).size().isEqualTo(4);
        assertThat(contains.stream().map(d -> d.getContent().getSentence())).contains("summer", "summer vacation", "hot summer", "I like summer.");
    }
}