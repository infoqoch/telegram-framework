package infoqoch.dictionarybot.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;

public class MemoryDictionaryRepository implements DictionaryRepository {

    private final Map<Long, Dictionary> repository = new HashMap<>();

    @Override
    public Long save(Dictionary dictionary) {
        final Dictionary result = Dictionary.builder().no(maxNo()).content(dictionary.getContent()).build();
        repository.put(result.getNo(), result);
        return result.getNo();
    }

    @Override
    public Optional<Dictionary> findByNo(Long dictionaryNo) {
        return Optional.ofNullable(repository.get(dictionaryNo));
    }

    private Long maxNo() {
        final OptionalLong max = repository.keySet().stream().mapToLong(l -> l).max();
        return max.orElse(1l);
    }
}
