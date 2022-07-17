package infoqoch.dictionarybot.model.dictionary.service;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import infoqoch.dictionarybot.model.dictionary.repository.LookupRepository;
import infoqoch.dictionarybot.model.user.ChatUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class DictionaryRandomTest {
    @Autowired EntityManager em;
	@Autowired
	LookupRepository repository;

	ChatUser chatUser = null;

    @BeforeEach
    void word_setUp() {
		chatUser = new ChatUser(ThreadLocalRandom.current().nextLong(), "kim");
		em.persist(chatUser);

        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().word("summer").build())); // exact match
        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().word("summer vacation").build())); // startsWith
        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().word("hot summer").build())); // endsWith
        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().word("I like summer.").build())); // contains
        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().word("winter").build())); // etc

        final TypedQuery<Dictionary> query = em.createQuery("select d from Dictionary d where d.content.word = :value", Dictionary.class);
        query.setParameter("value", "summer");
        final List<Dictionary> resultList = query.getResultList();
        assert resultList.size()==1;
    }
	@Test
	void random_no_data() {
		final Optional<Dictionary> random = repository.getRandom(randomUser());
		assertThat(random).isEmpty();
	}

	@Test
	void random_mine(){
		// given
		// target chatuser는 summer, winter를 가진 사전을 등록한다.
		// 그 외 uuid로 생성한 데이터를 등록하여, 이것은 summer가 들어가지 않는다고 상정한다.
		for(int i=0; i<100; i++) em.persist(randomDictionary());
		em.flush();
		em.clear();

		System.out.println("======== after flush ==========");

		// then
		for(int i=0; i<100; i++){
			final Optional<Dictionary> random = repository.getRandom(chatUser);
			assertThat(random).isPresent();
			assertThat(random.get().getContent().getWord()).containsAnyOf("summer", "winter");
		}
	}

	private Dictionary randomDictionary() {
		return new Dictionary(null, randomUser(), null, DictionaryContent.builder().word(UUID.randomUUID().toString()).build());
	}

	private ChatUser randomUser() {
		final ChatUser user = new ChatUser(ThreadLocalRandom.current().nextLong(), UUID.randomUUID().toString().substring(0, 4));
		em.persist(user);
		return user;
	}
}
