package infoqoch.telegramframework.spring.sampleinspring.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpdateLogRepository extends JpaRepository<UpdateLog, Long> {
    List<UpdateLog> findByChatId(Long chatId);
}
