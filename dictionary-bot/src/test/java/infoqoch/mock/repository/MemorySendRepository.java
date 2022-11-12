package infoqoch.mock.repository;

import infoqoch.dictionarybot.log.send.SendLog;
import infoqoch.dictionarybot.log.send.repository.SendRepository;
import infoqoch.telegram.framework.update.response.SendType;
import infoqoch.telegram.framework.update.send.Send;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class MemorySendRepository implements SendRepository {
    private final Map<Long, SendLog> repository = new HashMap<>();

    @Override
    public SendLog save(SendLog sendLog) {
        final Long no = maxNo();

        setNoWithReflect(sendLog, no);

        repository.put(no, sendLog);

        return sendLog;
    }

    @Override
    public List<SendLog> findByStatus(Send.Status status) {
        return repository.values().stream().filter(s -> s.getStatus()==status).collect(Collectors.toList());
    }

    @Override
    public Optional<SendLog> findByNo(Long no) {
        return Optional.empty();
    }

    @Override
    public List<SendLog> findAll() {
        return repository.values().stream().toList();
    }


    @SneakyThrows
    private void setNoWithReflect(SendLog sendLog, Long no) {
        Field noField = sendLog.getClass().getDeclaredField("no");
        noField.setAccessible(true);
        noField.set(sendLog, no);
    }

    @Override
    public Long maxNo() {
        final OptionalLong max = repository.keySet().stream().mapToLong(l -> l).max();

        if(max.isPresent())
            return max.getAsLong() + 1l;
        return 1l;
    }

    // TODO
    // not yet implemented
    @Override
    public List<SendLog> findByNoGreaterThanAndSendType(Long no, SendType sendType) {
        new UnsupportedOperationException("not support operation");
        return null;
    }
}
