package infoqoch.mock.repository;

import infoqoch.dictionarybot.log.send.Send;
import infoqoch.telegram.framework.update.response.SendType;
import infoqoch.dictionarybot.log.send.repository.SendRepository;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class MemorySendRepository implements SendRepository {
    private final Map<Long, Send> repository = new HashMap<>();

    @Override
    public Send save(Send send) {
        final Long no = maxNo();

        setNoWithReflect(send, no);

        repository.put(no, send);

        return send;
    }

    @Override
    public List<Send> findByStatus(Send.Status status) {
        return repository.values().stream().filter(s -> s.getStatus()==status).collect(Collectors.toList());
    }

    @Override
    public Optional<Send> findByNo(Long no) {
        return Optional.empty();
    }

    @Override
    public List<Send> findAll() {
        return repository.values().stream().toList();
    }


    @SneakyThrows
    private void setNoWithReflect(Send send, Long no) {
        Field noField = send.getClass().getDeclaredField("no");
        noField.setAccessible(true);
        noField.set(send, no);
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
    public List<Send> findByNoGreaterThanAndRequestSendType(Long no, SendType status) {
        new UnsupportedOperationException("not support operation");
        return null;
    }
}
