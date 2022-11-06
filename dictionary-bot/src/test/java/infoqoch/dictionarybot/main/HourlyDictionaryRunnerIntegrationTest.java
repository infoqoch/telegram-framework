package infoqoch.dictionarybot.main;

import infoqoch.dictionarybot.HourlyDictionaryRunner;
import infoqoch.dictionarybot.FakeSendRequestEventListener;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import infoqoch.dictionarybot.model.dictionary.DictionaryContentMarkdownPrinter;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.send.Send;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles({"test", "fake_send_listener"})
@SpringBootTest
@Transactional
class HourlyDictionaryRunnerIntegrationTest {
    @Autowired EntityManager em;

    // FAKE
    @Autowired FakeSendRequestEventListener fakeSendRequestEventListener;

    // 타겟 객체
    @Autowired
    HourlyDictionaryRunner runner;

    @BeforeEach
    void setUp(){
        fakeSendRequestEventListener.clear();
    }

    @DisplayName("유저와 사전 모두 없어 특별한 동작을 하지 않는다.")
    @Test
    void no_result(){
        // given
        assert !fakeSendRequestEventListener.isCalled();

        // when
        runner.hourlyDictionaryRun(); // 회원이 없으므로 보낸 데이터가 없다.

        // then
        assert !fakeSendRequestEventListener.isCalled();
    }

    @DisplayName("ChatUser가 혼자이며 자신의 사전을 받는다.")
    @Test
    void only_one_user(){
        // given
        ChatUser chatUser = createChatUserAndPushDictionary(true);

        // when
        runner.hourlyDictionaryRun();

        // then
        assertThat(fakeSendRequestEventListener.isCalled()).isTrue();
        final Send sent = fakeSendRequestEventListener.getLatestSent();
        assertThat(
                sent.getRequest().getMessage().toString()
        ).isEqualTo(
                new MarkdownStringBuilder().bold("=정시의 영어단어장!=").lineSeparator().append(new DictionaryContentMarkdownPrinter(chatUser.getDictionaries().get(0)).toMarkdown()).toString()
        );
    }

    @DisplayName("ChatUser가 타인의 사전을 받는다.")
    @Test
    void get_others(){
        // given
        ChatUser someone = createChatUserAndPushDictionary(false); // 대상 유저만 스케줄링하도록, 다른 회원은 false로 만든다.
        ChatUser chatUser = new ChatUser(someone.getChatId()-1, "lee");
        chatUser.setHourlyAlarm(true);
        chatUser.setLookupAllUsers(true);
        em.persist(chatUser);
        em.flush();
        em.clear();

        // when
        runner.hourlyDictionaryRun();

        // then
        assertThat(fakeSendRequestEventListener.isCalled()).isTrue();
        final Send sent = fakeSendRequestEventListener.getLatestSent();
        assertThat(
                sent.getRequest().getMessage().toString()
        ).isEqualTo(
                new MarkdownStringBuilder().bold("=정시의 영어단어장!=").lineSeparator().append(new DictionaryContentMarkdownPrinter(someone.getDictionaries().get(0)).toMarkdown()).toString()
        );

    }

    @DisplayName("ChatUser가 오직 나만의 사전만 받는다.")
    @Test
    void get_only_mine(){
        // given
        ChatUser someone = createChatUserAndPushDictionary(false); // 대상 유저만 스케줄링하도록, 다른 회원은 false로 만든다.
        ChatUser chatUser = new ChatUser(someone.getChatId()-1, "lee");
        chatUser.setHourlyAlarm(true);
        chatUser.setLookupAllUsers(false); // 내 것만 받는다.
        em.persist(chatUser);
        em.flush();
        em.clear();

        // when
        runner.hourlyDictionaryRun();

        // then
        assertThat(fakeSendRequestEventListener.isCalled()).isTrue();
        final Send sent = fakeSendRequestEventListener.getLatestSent();
        assertThat(
                sent.getRequest().getMessage().toString()
        ).isEqualTo(
                new MarkdownStringBuilder().bold("=정시의 영어단어장!=").lineSeparator().plain("아직 등록한 사전이 없습니다!").toString()
        );
    }

    private ChatUser createChatUserAndPushDictionary(boolean hourlyAlarm) {
        ChatUser chatUser = new ChatUser(ThreadLocalRandom.current().nextLong(), "kim");
        chatUser.setHourlyAlarm(hourlyAlarm);
        em.persist(chatUser);
        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().word("summer").build()));
        return chatUser;
    }

}