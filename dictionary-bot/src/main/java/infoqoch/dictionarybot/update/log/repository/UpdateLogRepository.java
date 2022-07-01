package infoqoch.dictionarybot.update.log.repository;

import infoqoch.dictionarybot.update.log.UpdateLog;

import java.util.List;

public interface UpdateLogRepository {
    UpdateLog save(UpdateLog updateLog);
    List<UpdateLog> findAll();
}
