package infoqoch.dictionarybot.mock.repository;

import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.repository.SendRepository;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Primary
@Profile("!test_jpa")
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

    @Override
    public Optional<Send> findByNo(Long no) {
        return Optional.ofNullable(repository.get(no));
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
