package infoqoch.dictionarybot.send.repository;

import infoqoch.dictionarybot.send.Send;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendJpaRepository extends JpaRepository<Send, Long>, SendRepository {

}
