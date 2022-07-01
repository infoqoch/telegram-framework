package infoqoch.dictionarybot.send.repository;

import infoqoch.dictionarybot.send.Send;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
import java.util.stream.Collectors;

@Repository
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

    @SneakyThrows
    private void setNoWithReflect(Send send, Long no) {
        Field noField = send.getClass().getDeclaredField("no");
        noField.setAccessible(true);
        noField.set(send, no);
    }

    private Long maxNo() {
        final OptionalLong max = repository.keySet().stream().mapToLong(l -> l).max();

        if(max.isPresent())
            return max.getAsLong() + 1l;
        return 1l;
    }
}
