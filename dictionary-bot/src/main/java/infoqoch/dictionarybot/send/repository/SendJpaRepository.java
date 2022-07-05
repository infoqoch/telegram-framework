package infoqoch.dictionarybot.send.repository;

import infoqoch.dictionarybot.send.Send;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Profile("!test")
@Repository
public interface SendJpaRepository extends SendRepository, JpaRepository<Send, Long> {

}
