package infoqoch.dictionarybot.log.send.repository;

import infoqoch.dictionarybot.log.send.SendLog;
import infoqoch.telegram.framework.update.response.SendType;
import infoqoch.telegram.framework.update.send.Send;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SendRepository {
    SendLog save(SendLog sendLog);

    @Query(" select max(s.no) from SendLog s")
    Long maxNo();

    List<SendLog> findByNoGreaterThanAndSendType(Long no, SendType sendType);

    List<SendLog> findByStatus(Send.Status status);

    Optional<SendLog> findByNo(Long no);

    List<SendLog> findAll();
}
