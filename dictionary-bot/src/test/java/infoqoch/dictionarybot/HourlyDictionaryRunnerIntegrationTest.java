package infoqoch.dictionarybot;

import infoqoch.dictionarybot.integration.dictionarybot.FakeSendRequestEventListener;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.send.Send;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
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

    @Test
    void get_mine(){
        // given
        ChatUser chatUser = createChatUserAndPushDictionary(true);

        // when
        runner.hourlyDictionaryRun();

        // then
        assertThat(fakeSendRequestEventListener.isCalled()).isTrue();
        final Send sent = fakeSendRequestEventListener.getSavedSend();
        assertThat(
                sent.getRequest().getMessage().toString()
        ).isEqualTo(
                new MarkdownStringBuilder().bold("=정시의 영어단어장!=").lineSeparator().append(chatUser.getDictionaries().get(0).toMarkdown()).toString()
        );
    }

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
        final Send sent = fakeSendRequestEventListener.getSavedSend();
        assertThat(
                sent.getRequest().getMessage().toString()
        ).isEqualTo(
                new MarkdownStringBuilder().bold("=정시의 영어단어장!=").lineSeparator().append(someone.getDictionaries().get(0).toMarkdown()).toString()
        );
    }

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
        final Send sent = fakeSendRequestEventListener.getSavedSend();
        assertThat(
                sent.getRequest().getMessage().toString()
        ).isEqualTo(
                new MarkdownStringBuilder().bold("=정시의 영어단어장!=").lineSeparator().plain("아직 등록한 사전이 없습니다!").toString()
        );
    }

    @Test
    void no_result(){
        // given
        assert !fakeSendRequestEventListener.isCalled();

        // when
        runner.hourlyDictionaryRun(); // 회원이 없으므로 보낸 데이터가 없다.

        // then
        assert !fakeSendRequestEventListener.isCalled();
    }

    private ChatUser createChatUserAndPushDictionary(boolean hourlyAlarm) {
        ChatUser chatUser = new ChatUser(ThreadLocalRandom.current().nextLong(), "kim");
        chatUser.setHourlyAlarm(hourlyAlarm);
        em.persist(chatUser);
        em.persist(new Dictionary(null, chatUser, null, DictionaryContent.builder().word("summer").build()));
        return chatUser;
    }

}