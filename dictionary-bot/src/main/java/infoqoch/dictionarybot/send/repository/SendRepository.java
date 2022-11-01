package infoqoch.dictionarybot.send.repository;

import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.update.response.SendType;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SendRepository {
    Send save(Send send);

    @Query(" select max(s.no) from Send s")
    Long maxNo();

    List<Send> findByNoGreaterThanAndRequestSendType(Long no, SendType status);

    List<Send> findByStatus(Send.Status status);

    Optional<Send> findByNo(Long no);

    List<Send> findAll();
}
