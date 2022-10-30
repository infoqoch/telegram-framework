package infoqoch.dictionarybot.send.repository;

import infoqoch.dictionarybot.mock.repository.QuerydslConfig;
import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.SendRequest;
import infoqoch.dictionarybot.send.SendType;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

import static infoqoch.dictionarybot.send.SendType.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class SendJpaRepositoryTest {
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
        assertThat(sendRepository.findByNoGreaterThanAndRequestSendType(first, MESSAGE)).size().isEqualTo(2);
        assertThat(sendRepository.findByNoGreaterThanAndRequestSendType(first, DOCUMENT)).size().isEqualTo(2);
        assertThat(sendRepository.findByNoGreaterThanAndRequestSendType(first, SERVER_ERROR)).size().isEqualTo(1);
        assertThat(sendRepository.findByNoGreaterThanAndRequestSendType(first, CLIENT_ERROR)).size().isEqualTo(3);
    }


    private void saveSend(SendType...types) {
        for (SendType type : types) {
             Send send = Send.of(SendRequest.send(123l, type, new MarkdownStringBuilder("hi"), null));
             em.persist(send);
        }
        em.flush();
        em.clear();
    }



}