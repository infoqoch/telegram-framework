package infoqoch.dictionarybot.model.dictionary.service;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@ActiveProfiles("dev")
@SpringBootTest
public class LookupServiceTest {
    @Autowired
    EntityManager em;

    @BeforeEach
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

    @Autowired
    LookupService service;

    @Test
	void contains_exactly() {
		List<Dictionary> result = service.word("summer", 3, 0);
		assertThat(result).size().isEqualTo(3);
		assertThat(result.get(0).getContent().getWord()).isEqualTo("summer");
		assertThat(result.get(1).getContent().getWord()).isEqualTo("summer vacation");
		assertThat(result.get(2).getContent().getWord()).isEqualTo("hot summer.");
	}

	@Test
	void exact_and_startWith() {
		List<Dictionary> result = service.word("summer", 2, 0);
		assertThat(result).size().isEqualTo(2);
		assertThat(result.get(0).getContent().getWord()).isEqualTo("summer");
		assertThat(result.get(1).getContent().getWord()).isEqualTo("summer vacation");
	}

	@Test
	void exact() {
		List<Dictionary> result = service.word("summer", 1, 0);
		assertThat(result).size().isEqualTo(1);
		assertThat(result.get(0).getContent().getWord()).isEqualTo("summer");
	}

	@Test
	void limit_0() {
		assertThatThrownBy(()->{
			service.word("summer", 0, 0);
		}).isExactlyInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void limit_over_total_count() {
		List<Dictionary> result = service.word("summer", 999999, 0);
		assertThat(result).size().isEqualTo(4);
		assertThat(result.get(0).getContent().getWord()).isEqualTo("summer");
        assertThat(result.get(1).getContent().getWord()).isEqualTo("summer vacation");
        // 순서는 입력한 순서로 들어가므로, 원칙적으로 보장하는 것을 기대할 수 없지만 그렇게 가정하고 하였음.
        assertThat(result.get(2).getContent().getWord()).isEqualTo("hot summer");
		assertThat(result.get(3).getContent().getWord()).isEqualTo("I like summer.");
	}

	@Test
	void offset_summer_second() {
		List<Dictionary> result = service.word("summer", 1, 1);
		assertThat(result).size().isEqualTo(1);
		assertThat(result.get(0).getContent().getWord()).isEqualTo("summer vacation");
	}
}
