package infoqoch.dictionarybot.model.dictionary.repository;


import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@Disabled @Deprecated
@SpringBootTest
@Transactional
class DictionaryQueryRepositoryTest {
//    @Autowired EntityManager em;
//
//    @Autowired
//    DictionaryQueryRepository repository;
//
//    ChatUser chatUser = null;
//
//    void word_setUp() {
//        chatUser = new ChatUser(ThreadLocalRandom.current().nextLong(), "kim");
//        em.persist(chatUser);
//
//        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer").build())); // exact match
//        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("summer vacation").build())); // startsWith
//        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("hot summer").build())); // endsWith
//        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("I like summer.").build())); // contains
//        em.persist(new Dictionary(null, chatUser, null,  DictionaryContent.builder().word("winter").build())); // etc
//
//        final TypedQuery<Dictionary> query = em.createQuery("select d from Dictionary d where d.content.word = :value", Dictionary.class);
//        query.setParameter("value", "summer");
//        final List<Dictionary> resultList = query.getResultList();
//        assert resultList.size()==1;
//    }
//
//    @Test
//    void findByWord() {
//        word_setUp();
//
//        final List<Dictionary> exact = repository.findByContentWord(chatUser, "summer");
//        assertThat(exact).size().isEqualTo(1);
//        assertThat(exact.get(0).getContent().getWord()).isEqualTo("summer");
//
//        final List<Dictionary> start = repository.findByContentWordStartsWith(chatUser, "summer");
//        assertThat(start).size().isEqualTo(2);
//        assertThat(start.stream().map(d -> d.getContent().getWord())).contains("summer", "summer vacation");
//
//        final List<Dictionary> end = repository.findByContentWordEndsWith(chatUser, "summer");
//        assertThat(end).size().isEqualTo(2);
//        assertThat(end.stream().map(d -> d.getContent().getWord())).contains("summer", "hot summer");
//
//        final List<Dictionary> contains = repository.findByContentWordContains(chatUser, "summer");
//        assertThat(contains).size().isEqualTo(4);
//        assertThat(contains.stream().map(d -> d.getContent().getWord())).contains("summer", "summer vacation", "hot summer", "I like summer.");
//    }
//
//    void definition_setUp() {
//        ChatUser chatUser = new ChatUser(ThreadLocalRandom.current().nextLong(), "kim");
//        em.persist(chatUser);
//
//        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().definition("summer").build())); // exact match
//        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().definition("summer vacation").build())); // startsWith
//        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().definition("hot summer").build())); // endsWith
//        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().definition("I like summer.").build())); // contains
//        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().definition("winter").build())); // etc
//    }
//
//    @Test
//    void findByDefinition() {
//        definition_setUp();
//
//        final List<Dictionary> exact = repository.findByContentDefinition(chatUser, "summer");
//        assertThat(exact).size().isEqualTo(1);
//        assertThat(exact.get(0).getContent().getDefinition()).isEqualTo("summer");
//
//        final List<Dictionary> start = repository.findByContentDefinitionStartsWith(chatUser, "summer");
//        assertThat(start).size().isEqualTo(2);
//        assertThat(start.stream().map(d -> d.getContent().getDefinition())).contains("summer", "summer vacation");
//
//        final List<Dictionary> end = repository.findByContentDefinitionEndsWith(chatUser, "summer");
//        assertThat(end).size().isEqualTo(2);
//        assertThat(end.stream().map(d -> d.getContent().getDefinition())).contains("summer", "hot summer");
//
//        final List<Dictionary> contains = repository.findByContentDefinitionContains(chatUser, "summer");
//        assertThat(contains).size().isEqualTo(4);
//        assertThat(contains.stream().map(d -> d.getContent().getDefinition())).contains("summer", "summer vacation", "hot summer", "I like summer.");
//    }
//
//
//    void sentence_setUp() {
//        final ChatUser chatUser = new ChatUser(ThreadLocalRandom.current().nextLong(), "kim");
//        em.persist(chatUser);
//
//        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().sentence("summer").build())); // exact match
//        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().sentence("summer vacation").build())); // startsWith
//        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().sentence("hot summer").build())); // endsWith
//        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().sentence("I like summer.").build())); // contains
//        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().sentence("winter").build())); // etc
//    }
//
//    @Test
//    void findBySentence() {
//        sentence_setUp();
//
//        final List<Dictionary> exact = repository.findByContentSentence(chatUser, "summer");
//        assertThat(exact).size().isEqualTo(1);
//        assertThat(exact.get(0).getContent().getSentence()).isEqualTo("summer");
//
//        final List<Dictionary> start = repository.findByContentSentenceStartsWith(chatUser, "summer");
//        assertThat(start).size().isEqualTo(2);
//        assertThat(start.stream().map(d -> d.getContent().getSentence())).contains("summer", "summer vacation");
//
//        final List<Dictionary> end = repository.findByContentSentenceEndsWith(chatUser, "summer");
//        assertThat(end).size().isEqualTo(2);
//        assertThat(end.stream().map(d -> d.getContent().getSentence())).contains("summer", "hot summer");
//
//        final List<Dictionary> contains = repository.findByContentSentenceContains(chatUser, "summer");
//        assertThat(contains).size().isEqualTo(4);
//        assertThat(contains.stream().map(d -> d.getContent().getSentence())).contains("summer", "summer vacation", "hot summer", "I like summer.");
//    }
}