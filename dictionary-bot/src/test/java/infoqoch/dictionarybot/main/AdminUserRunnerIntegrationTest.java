package infoqoch.dictionarybot.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

// AdminUserRunner를 테스트한다.
// AdminUserRunner는 SendRunner 중 SERVER_ERROR을 타입으로 하는 값에 대하여 반응한다. 해당 데이터가 존재할 때 Admin에 얼럿을 보낸다.
// 그러므로 대체로 이벤트 리스너에 대한 테스트가 주가 된다.
@Slf4j
@ActiveProfiles({"test", "fake_send_listener"})
@SpringBootTest
@Transactional
class AdminUserRunnerIntegrationTest {
//    @Autowired EntityManager em;
//
//    // FAKE
//    @Autowired
//    FakeSendRequestEventListener fakeSendRequestEventListener;
//
//    // 타겟 객체
//    @Autowired
//    AdminUserRunner runner;
//
//    @Autowired
//    BeanFactory beanFactory;
//
//    @Autowired
//    SendRepository sendRepository;
//
//    @Autowired
//    ApplicationContext applicationContext;
//
//    @BeforeEach
//    void setUp(){
//        EventsConfigurationSupporter.replaceApplicationContext(applicationContext);
//
//        createAdminUser();
//        fakeSendRequestEventListener.clear();
//    }
//
//
//    @DisplayName("Send 데이터가 없음")
//    @Test
//    void no_result(){
//        // when
//        runner.run();
//
//        // then
//        assertThat(fakeSendRequestEventListener.isCalled()).isFalse();
//    }
//
//    @DisplayName("Send 데이터가 있으나, SERVER_ERROR가 없음")
//    @Test
//    void no_error(){
//        // given
//        raiseSendEvent(DOCUMENT, CLIENT_ERROR, MESSAGE, DOCUMENT, CLIENT_ERROR, CLIENT_ERROR, MESSAGE); // 7개
//        em.flush();
//
//        // when
//        runner.run();
//
//        // then
//        assertThat(fakeSendRequestEventListener.isCalled()).isTrue();
//        final List<SendLog> sentList = fakeSendRequestEventListener.getSentList();
//        assertThat(sentList).size().isEqualTo(7);
//
//        final List<SendLog> serverError = sentList.stream().filter(s -> s.getRequest().getSendType().equals(SERVER_ERROR)).collect(Collectors.toList());
//        assertThat(serverError).size().isEqualTo(0);
//
//        final List<SendLog> message = sentList.stream().filter(s -> s.getRequest().getSendType().equals(MESSAGE)).collect(Collectors.toList());
//        assertThat(message).size().isEqualTo(2);
//    }
//
//    @DisplayName("Send 데이터가 있으며, SERVER_ERROR가 있다. AdminRunner가 반응한다.")
//    @Test
//    void server_error(){
//        // given
//        raiseSendEvent(DOCUMENT, SERVER_ERROR); // 2개
//        em.flush();
//
//        // when
//        runner.run();
//
//        // then
//        // 호출 검토
//        assertThat(fakeSendRequestEventListener.isCalled()).isTrue();
//        final List<SendLog> sentList = fakeSendRequestEventListener.getSentList();
//        assertThat(sentList).size().isEqualTo(3); // ADMIN_ALERT이 추가된다.
//
//        final List<SendLog> serverError = sentList.stream().filter(s -> s.getRequest().getSendType().equals(SERVER_ERROR)).collect(Collectors.toList());
//        assertThat(serverError).size().isEqualTo(1);
//
//        final List<SendLog> adminAlert = sentList.stream().filter(s -> s.getRequest().getSendType().equals(ADMIN_ALERT)).collect(Collectors.toList());
//        assertThat(adminAlert).size().isEqualTo(1);
//
//        // 메시지 검토
//        final MarkdownStringBuilder message = adminAlert.get(0).getRequest().getMessage();
//        assertThat(message.toString()).contains(
//                new MarkdownStringBuilder().bold("=어드민 경고 알림!=").toString(),
//                new MarkdownStringBuilder().plain("sent message : [").append(new MarkdownStringBuilder("hi")).plain("]").toString()
//        );
//        System.out.println("message = " + message);
//    }
//
//    private ChatUser createAdminUser() {
//        final ChatUser chatUser = new ChatUser(123l, "kim");
//        chatUser.changeRole(ChatUser.Role.ADMIN);
//        em.persist(chatUser);
//        em.flush();
//        em.clear();
//        final ChatUser findUser = em.find(ChatUser.class, chatUser.getNo());
//        assert findUser.getRole() == ChatUser.Role.ADMIN;
//        assert ((Long) em.createQuery("select count(c.chatId) from ChatUser c").getSingleResult()) == 1l;
//        return findUser;
//    }
//
//    private void raiseSendEvent(SendType...types) {
//        for (SendType type : types) {
//            SendLog sendLog = SendLog.of(
//                    SendRequest.send(123l, type, new MarkdownStringBuilder("hi"), null)
//                    , null);
//            Events.raise(sendLog);
//        }
//    }
}