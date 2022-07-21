package infoqoch.dictionarybot.send.repository;

import infoqoch.dictionarybot.send.Send;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface SendRepository {
    Send save(Send send);
    List<Send> findByStatus(Send.Status status);

    @QueryHints({
            @QueryHint(name = org.hibernate.annotations.QueryHints.COMMENT, value = "ignore-logging")
    })
    @Query(" select s from Send s where s.status = :status ")
    List<Send> findByStatusForScheduler(@Param("status") Send.Status status);
    Optional<Send> findByNo(Long no);
}
