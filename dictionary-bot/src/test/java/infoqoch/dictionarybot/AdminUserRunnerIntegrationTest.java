package infoqoch.dictionarybot;

import infoqoch.dictionarybot.integration.dictionarybot.FakeSendRequestEventListener;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.SendRequest;
import infoqoch.dictionarybot.send.SendType;
import infoqoch.dictionarybot.system.event.Events;
import infoqoch.dictionarybot.update.log.UpdateLog;
import infoqoch.dictionarybot.update.request.UpdateRequestCommand;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

import static infoqoch.dictionarybot.send.SendType.*;
import static infoqoch.dictionarybot.send.SendType.MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"test", "fake_send_listener"})
@SpringBootTest
@Transactional
class AdminUserRunnerIntegrationTest {
    @Autowired EntityManager em;

    // FAKE
    @Autowired FakeSendRequestEventListener fakeSendRequestEventListener;

    // 타겟 객체
    @Autowired
    AdminUserRunner runner;

    @BeforeEach
    void setUp(){
        // 각 테스트는 하나의 컨텍스트를 돌려 쓴다. 트랜잭션은 아마 db에 대한 롤백은 진행하는 것 같다. 그 외의 빈에 대해서는 초기화를 해야 한다.
        fakeSendRequestEventListener.clear();

    }

    @Test
    void no_result(){
        // when
        runner.run();

        // then
        assertThat(fakeSendRequestEventListener.isCalled()).isFalse();
    }


    private void raiseSendEvent(SendType...types) {
        for (SendType type : types) {
            Send send = Send.of(
                    SendRequest.send(123l, type, new MarkdownStringBuilder("hi"), null)
                    , null);
            Events.raise(send);
        }
    }


    @Test
    void no_error(){
        // given
        raiseSendEvent(DOCUMENT, CLIENT_ERROR, MESSAGE, DOCUMENT, CLIENT_ERROR, CLIENT_ERROR, MESSAGE); // 7개

        // when
        runner.run();

        // then
        assertThat(fakeSendRequestEventListener.isCalled()).isTrue();
        final List<Send> sentList = fakeSendRequestEventListener.getSentList();
        assertThat(sentList).size().isEqualTo(7);

        final List<Send> serverError = sentList.stream().filter(s -> s.getRequest().getSendType().equals(SERVER_ERROR)).collect(Collectors.toList());
        assertThat(serverError).size().isEqualTo(0);

        final List<Send> message = sentList.stream().filter(s -> s.getRequest().getSendType().equals(MESSAGE)).collect(Collectors.toList());
        assertThat(message).size().isEqualTo(2);
    }

    @Test
    void server_error(){
        // given
        raiseSendEvent(DOCUMENT, SERVER_ERROR); // 2개
        ChatUser adminUser = createAdminUser();

        // when
        runner.run();

        // then
        // 호출 검토
        assertThat(fakeSendRequestEventListener.isCalled()).isTrue();
        final List<Send> sentList = fakeSendRequestEventListener.getSentList();
        assertThat(sentList).size().isEqualTo(3); // ADMIN_ALERT이 추가된다.

        final List<Send> serverError = sentList.stream().filter(s -> s.getRequest().getSendType().equals(SERVER_ERROR)).collect(Collectors.toList());
        assertThat(serverError).size().isEqualTo(1);

        final List<Send> adminAlert = sentList.stream().filter(s -> s.getRequest().getSendType().equals(ADMIN_ALERT)).collect(Collectors.toList());
        assertThat(adminAlert).size().isEqualTo(1);
        
        // 메시지 검토
        final MarkdownStringBuilder message = adminAlert.get(0).getRequest().getMessage();
        assertThat(message.toString()).contains(
                new MarkdownStringBuilder().bold("=어드민 경고 알림!=").toString(),
                new MarkdownStringBuilder().plain("sent message : [").append(new MarkdownStringBuilder("hi")).plain("]").toString()
        );
        System.out.println("message = " + message);
    }

    @Test
    void has_update_log() {
        // given
        final UpdateLog updateLog = UpdateLog.builder().updateId(1235l).updateCommand(UpdateRequestCommand.LOOKUP_DEFINITION).updateValue("abc").build();
        em.persist(updateLog);

        Send send = Send.of(
                SendRequest.send(123l, SERVER_ERROR, new MarkdownStringBuilder("hi"), null)
                , updateLog);
        Events.raise(send);

        ChatUser adminUser = createAdminUser();

        // when
        runner.run();

        // then
        assertThat(fakeSendRequestEventListener.isCalled()).isTrue();
        final List<Send> sentList = fakeSendRequestEventListener.getSentList();
        assertThat(sentList).size().isEqualTo(2); // ADMIN_ALERT이 추가된다.

        final List<Send> adminAlert = sentList.stream().filter(s -> s.getRequest().getSendType().equals(ADMIN_ALERT)).collect(Collectors.toList());
        assertThat(adminAlert).size().isEqualTo(1);

        final MarkdownStringBuilder message = adminAlert.get(0).getRequest().getMessage();
        assertThat(message.toString()).contains(
                new MarkdownStringBuilder().bold("=어드민 경고 알림!=").toString(),
                new MarkdownStringBuilder().plain("sent message : [").append(new MarkdownStringBuilder("hi")).plain("]").toString(),
                new MarkdownStringBuilder().italic("caused by update id : ").italic("1235").toString(),
                new MarkdownStringBuilder().plain("  -> command : ").plain(UpdateRequestCommand.LOOKUP_DEFINITION.toString()).plain(" : ").plain("abc").toString()
        );
        System.out.println("message = " + message);
    }

    private ChatUser createAdminUser() {
        final ChatUser chatUser = new ChatUser(123l, "kim");
        chatUser.changeRole(ChatUser.Role.ADMIN);
        em.persist(chatUser);
        em.flush();
        em.clear();
        final ChatUser findUser = em.find(ChatUser.class, chatUser.getNo());
        assert findUser.getRole() == ChatUser.Role.ADMIN;
        return findUser;
    }
}