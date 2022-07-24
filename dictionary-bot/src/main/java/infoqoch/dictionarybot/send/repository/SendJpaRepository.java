package infoqoch.dictionarybot.send.repository;

import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.SendType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SendJpaRepository extends SendRepository, JpaRepository<Send, Long> {
    @Query(" select max(s.no) from Send s")
    Long maxNo();

    List<Send> findByNoGreaterThanAndRequestSendType(Long no, SendType status);

    List<Send> findByStatus(Send.Status status);

    Optional<Send> findByNo(Long no);
}
