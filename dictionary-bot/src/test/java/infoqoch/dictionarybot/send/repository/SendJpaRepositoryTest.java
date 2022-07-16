package infoqoch.dictionarybot.send.repository;

import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.SendRequest;
import infoqoch.dictionarybot.send.SendType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static infoqoch.dictionarybot.send.SendType.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SendJpaRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    SendJpaRepository sendRepository;

    @Test
    void max(){
        // no data
        final Long noData = sendRepository.maxNo();
        assertThat(noData).isNull();

        // save one of data
        saveSend(MESSAGE);
        Long first = sendRepository.maxNo();
        assertThat(first).isNotNull();
        assertThat(first).isEqualTo(1l);

        // save one more
        saveSend(MESSAGE);
        Long second = sendRepository.maxNo();
        assertThat(second).isNotNull();
        assertThat(second).isGreaterThan(first);
    }

    private void saveSend(SendType...types) {
        for (SendType type : types) {
             Send send = Send.of(SendRequest.send(123l, type, new MarkdownStringBuilder("hi"), null));
             em.persist(send);
        }
        em.flush();
        em.clear();
    }


    @Test
    void find_by_no_and_type(){
        // given
        saveSend(MESSAGE);
        Long first = sendRepository.maxNo();

        // when
        saveSend(DOCUMENT, CLIENT_ERROR, MESSAGE, DOCUMENT, SERVER_ERROR, CLIENT_ERROR, CLIENT_ERROR, MESSAGE);
        assertThat(sendRepository.findByNoGreaterThanAndRequestSendType(first, MESSAGE)).size().isEqualTo(2);
        assertThat(sendRepository.findByNoGreaterThanAndRequestSendType(first, DOCUMENT)).size().isEqualTo(2);
        assertThat(sendRepository.findByNoGreaterThanAndRequestSendType(first, SERVER_ERROR)).size().isEqualTo(1);
        assertThat(sendRepository.findByNoGreaterThanAndRequestSendType(first, CLIENT_ERROR)).size().isEqualTo(3);
    }

}