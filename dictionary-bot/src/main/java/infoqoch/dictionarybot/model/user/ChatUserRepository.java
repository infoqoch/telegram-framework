package infoqoch.dictionarybot.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    Optional<ChatUser> findByChatId(Long chatId);

    List<ChatUser> findByHourlyAlarm(boolean hourlyAlarm);

    List<ChatUser> findByRole(ChatUser.Role role);
}
