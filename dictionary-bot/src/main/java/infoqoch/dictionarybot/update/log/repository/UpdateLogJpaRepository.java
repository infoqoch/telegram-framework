package infoqoch.dictionarybot.update.log.repository;

import infoqoch.dictionarybot.update.log.UpdateLog;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Profile("!test")
@Repository
public interface UpdateLogJpaRepository extends UpdateLogRepository, JpaRepository<UpdateLog, Long> {

}
