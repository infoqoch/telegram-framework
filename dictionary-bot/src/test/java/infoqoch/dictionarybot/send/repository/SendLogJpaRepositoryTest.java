package infoqoch.dictionarybot.send.repository;

import infoqoch.dictionarybot.log.send.SendLog;
import infoqoch.dictionarybot.log.send.repository.SendJpaRepository;
import infoqoch.telegram.framework.update.response.SendType;
import infoqoch.telegram.framework.update.send.Send;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static infoqoch.telegram.framework.update.response.SendType.*;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
class SendLogJpaRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    SendJpaRepository sendRepository;

    @DisplayName("auto increment의 정상 동작 여부 확인")
    @Test
    void max(){
        // no data
        final Long noData = sendRepository.maxNo();
        assertThat(noData).isNull();

        // save one of data
        saveSend(MESSAGE);
        Long first = sendRepository.maxNo();
        assertThat(first).isNotNull();
        assertThat(first).isGreaterThan(0l);

        // save one more
        saveSend(MESSAGE);
        Long second = sendRepository.maxNo();
        assertThat(second).isNotNull();
        assertThat(second).isGreaterThan(first);
    }

    @DisplayName("no(pk)와 SentType에 따른 검색 기능 동작 여부 확인. 메시지를 telegram에 보낼 때 아래의 검색기능을 활용한다.")
    @Test
    void find_by_no_and_type(){
        // given
        saveSend(MESSAGE);
        Long first = sendRepository.maxNo();

        // when
        saveSend(DOCUMENT, CLIENT_ERROR, MESSAGE, DOCUMENT, SERVER_ERROR, CLIENT_ERROR, CLIENT_ERROR, MESSAGE);
        assertThat(sendRepository.findByNoGreaterThanAndSendType(first, MESSAGE)).size().isEqualTo(2);
        assertThat(sendRepository.findByNoGreaterThanAndSendType(first, DOCUMENT)).size().isEqualTo(2);
        assertThat(sendRepository.findByNoGreaterThanAndSendType(first, SERVER_ERROR)).size().isEqualTo(1);
        assertThat(sendRepository.findByNoGreaterThanAndSendType(first, CLIENT_ERROR)).size().isEqualTo(3);
    }

    public void saveSend(SendType...types) {
        for (SendType type : types) {
            SendLog sendLog = SendLog.of(Send.send(123l, type, new MarkdownStringBuilder("hi"), null));
            em.persist(sendLog);
        }
        em.flush();
        em.clear();
    }
}