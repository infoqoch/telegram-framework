package infoqoch.dictionarybot.log.repository;

import infoqoch.dictionarybot.log.UpdateLog;

import java.util.List;

public interface UpdateLogRepository {
    UpdateLog save(UpdateLog updateLog);
    List<UpdateLog> findAll();
}
