package infoqoch.dictionarybot.log.update.repository;

import infoqoch.dictionarybot.log.update.UpdateLog;

import java.util.List;

public interface UpdateLogRepository {
    UpdateLog save(UpdateLog updateLog);
    List<UpdateLog> findAll();
}
