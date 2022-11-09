package infoqoch.dictionarybot.log.send.repository;

import infoqoch.dictionarybot.log.send.Send;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendJpaRepository extends JpaRepository<Send, Long>, SendRepository {

}
