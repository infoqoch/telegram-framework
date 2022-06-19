package infoqoch.dictionarybot.model.dictionary.repository;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

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
    public void save(List<Dictionary> dictionaries) {
        for (Dictionary dictionary : dictionaries) {
            save(dictionary);
        }
    }

    @Override
    public Optional<Dictionary> findByNo(Long dictionaryNo) {
        return Optional.ofNullable(repository.get(dictionaryNo));
    }

    @Override
    public List<Dictionary> findByWord(String target) {
        return repository.values().stream().filter(d -> d.getContent().getWord().contains(target)).collect(Collectors.toList());
    }

    @Override
    public List<Dictionary> findBySentence(String target) {
        return repository.values().stream().filter(d -> d.getContent().getSentence().contains(target)).collect(Collectors.toList());
    }

    @Override
    public List<Dictionary> findByDefinition(String target) {
        return repository.values().stream().filter(d -> d.getContent().getDefinition().contains(target)).collect(Collectors.toList());
    }

    @Override
    public List<Dictionary> findAll() {
         return repository.values().stream().collect(Collectors.toList());
    }

    private Long maxNo() {
        final OptionalLong max = repository.keySet().stream().mapToLong(l -> l).max();

        if(max.isPresent())
            return max.getAsLong() + 1l;
        return 1l;
    }
}
