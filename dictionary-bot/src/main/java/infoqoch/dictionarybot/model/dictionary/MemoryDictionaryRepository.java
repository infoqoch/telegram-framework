package infoqoch.dictionarybot.model.dictionary;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;

@Repository
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

    @Override
    public Optional<Dictionary> findByWord(String target) {
        return repository.values().stream().filter(d -> d.getContent().getWord().contains(target)).findAny();
    }

    private Long maxNo() {
        final OptionalLong max = repository.keySet().stream().mapToLong(l -> l).max();

        if(max.isPresent())
            return max.getAsLong() + 1l;
        return 1l;
    }
}
