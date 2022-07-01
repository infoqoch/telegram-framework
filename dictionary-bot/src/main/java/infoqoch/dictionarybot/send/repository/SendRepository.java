package infoqoch.dictionarybot.send.repository;

import infoqoch.dictionarybot.send.Send;

import java.util.List;

public interface SendRepository {
    Send save(Send send);
    List<Send> findByStatus(Send.Status status);
}
