package infoqoch.dictionarybot.log.update.repository;

import infoqoch.dictionarybot.log.update.UpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateLogJpaRepository extends UpdateLogRepository, JpaRepository<UpdateLog, Long> {

}
