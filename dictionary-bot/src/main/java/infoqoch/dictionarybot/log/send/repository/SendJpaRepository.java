package infoqoch.dictionarybot.log.send.repository;

import infoqoch.dictionarybot.log.send.SendLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendJpaRepository extends JpaRepository<SendLog, Long>, SendRepository {

}
