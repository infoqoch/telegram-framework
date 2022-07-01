package infoqoch.dictionarybot.send.repository;

import infoqoch.dictionarybot.send.Send;

import java.util.List;
import java.util.Optional;

public interface SendRepository {
    Send save(Send send);
    List<Send> findByStatus(Send.Status status);
    Optional<Send> findByNo(Long no);
}
