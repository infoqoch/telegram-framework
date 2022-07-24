package infoqoch.dictionarybot.model.dictionary.service;

import infoqoch.dictionarybot.mock.repository.QuerydslConfig;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import infoqoch.dictionarybot.model.dictionary.repository.LookupRepository;
import infoqoch.dictionarybot.model.user.ChatUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
public class DictionaryRandomJpaTest {
    @Autowired EntityManager em;
	@Autowired LookupRepository repository;

	@DisplayName("타인의 데이터는 존재하고, 탐색하려는 회원의 데이터는 없다")
	@Test
	void random_no_data() {
		// given
		final ChatUser noDataUser = createNoDataUser();

		// when
		final Optional<Dictionary> random = repository.getRandom(noDataUser);

		// then
		assertThat(random).isEmpty();
	}

	@DisplayName("타인의 데이터는 존재하고, 탐색하려는 회원의 데이터는 존재한다. 회원의 데이터만을 언제나 리턴해야 한다.")
	@Test
	void random_mine(){
		// given
		// 탐색하려는 유저 생성. summer라는 단어를 가진 데이터를 생성하였다.
		ChatUser chatUser = setup_user_and_dictionary_contains_summer();

		// 다른 회원의 데이터가 생성된다. summer 라는 단어가 존재하지 않는다.
		for(int i=0; i<100; i++) em.persist(randomDictionary());
		em.flush();
		em.clear();

		System.out.println("======== after flush ==========");

		// then
		// 검색한 단어에 언제나 summer가 나옴을 보장한다.
		int i=0;
		for(; i<100; i++){
			final Optional<Dictionary> random = repository.getRandom(chatUser);
			assertThat(random).isPresent();
			assertThat(random.get().getContent().getWord()).contains("summer");
		}
		assertThat(i).isEqualTo(100);
	}

	private ChatUser setup_user_and_dictionary_contains_summer() {
		ChatUser chatUser = new ChatUser(ThreadLocalRandom.current().nextLong(), "kim");
		em.persist(chatUser);

		em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().word("summer").build())); // exact match
		em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().word("summer vacation").build())); // startsWith
		em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().word("hot summer").build())); // endsWith
		em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().word("I like summer.").build())); // contains

		final TypedQuery<Dictionary> query = em.createQuery("select d from Dictionary d where d.content.word = :value", Dictionary.class);
		query.setParameter("value", "summer");
		final List<Dictionary> resultList = query.getResultList();
		assert resultList.size()==1;
		return chatUser;
	}

	// target chatuser는 summer, winter를 가진 사전을 등록한다.
	// 그 외 uuid로 생성한 데이터를 등록하여, 이것은 summer가 들어가지 않는다고 상정한다.
	private Dictionary randomDictionary() {
		final String random = createRandomWord();
		return new Dictionary(null, createNoDataUser(), null, DictionaryContent.builder().word(random).build());
	}

	private String createRandomWord() {
		while(true){
			String target = UUID.randomUUID().toString();
			if(target.contains("summer")) continue;
			return target;
		}
	}

	private ChatUser createNoDataUser() {
		final ChatUser user = new ChatUser(ThreadLocalRandom.current().nextLong(), UUID.randomUUID().toString().substring(0, 4));
		em.persist(user);
		return user;
	}
}
