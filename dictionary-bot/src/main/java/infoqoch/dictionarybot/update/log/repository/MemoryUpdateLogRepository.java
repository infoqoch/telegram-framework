package infoqoch.dictionarybot.update.log.repository;

import infoqoch.dictionarybot.update.log.UpdateLog;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
import java.util.stream.Collectors;

@Repository
public class MemoryUpdateLogRepository implements UpdateLogRepository {
    private final Map<Long, UpdateLog> repository = new HashMap<>();

    @Override
    public UpdateLog save(UpdateLog updateLog) {
        final Long no = maxNo();

        setNoWithReflect(updateLog, no);

        repository.put(no, updateLog);

        return updateLog;
    }

    @Override
    public List<UpdateLog> findAll() {
        return repository.values().stream().collect(Collectors.toList());
    }

    @SneakyThrows
    private void setNoWithReflect(UpdateLog updateLog, Long no) {
        Field noField = updateLog.getClass().getDeclaredField("no");
        noField.setAccessible(true); // private type 에 접근하기 위하여 필요.
        noField.set(updateLog, no); // boxing -> unboxing 은 가능하다, Long으로 boxing된 타입은 애당초 object로 인식하는듯.
    }

    private Long maxNo() {
        final OptionalLong max = repository.keySet().stream().mapToLong(l -> l).max();

        if(max.isPresent())
            return max.getAsLong() + 1l;
        return 1l;
    }
}
