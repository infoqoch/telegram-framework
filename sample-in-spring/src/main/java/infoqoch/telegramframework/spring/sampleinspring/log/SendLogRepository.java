package infoqoch.telegramframework.spring.sampleinspring.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendLogRepository extends JpaRepository<SendLog, Long> {

}
