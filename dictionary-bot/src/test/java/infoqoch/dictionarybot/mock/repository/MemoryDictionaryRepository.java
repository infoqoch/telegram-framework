package infoqoch.dictionarybot.mock.repository;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionarySource;
import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.model.user.ChatUser;

import java.util.*;
import java.util.stream.Collectors;

public class MemoryDictionaryRepository implements DictionaryRepository {

    private final Map<Long, Dictionary> repository = new HashMap<>();

    @Override
    public Dictionary save(Dictionary dictionary) {
        final Dictionary result = Dictionary.builder().no(maxNo()).chatUser(dictionary.getChatUser()).content(dictionary.getContent()).build();
        repository.put(result.getNo(), result);
        return result;
    }

    @Override
    public Optional<Dictionary> findByNo(Long dictionaryNo) {
        return Optional.ofNullable(repository.get(dictionaryNo));
    }

    @Override
    public List<Dictionary> findByContentWord(String target) {
        return repository.values().stream().filter(d -> d.getContent().getWord().contains(target)).collect(Collectors.toList());
    }

    @Override
    public List<Dictionary> findByContentSentence(String target) {
        return repository.values().stream().filter(d -> d.getContent().getSentence().contains(target)).collect(Collectors.toList());
    }

    @Override
    public List<Dictionary> findByContentDefinition(String target) {
        return repository.values().stream().filter(d -> d.getContent().getDefinition().contains(target)).collect(Collectors.toList());
    }

    @Override
    public List<Dictionary> findAll() {
         return repository.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<Dictionary> findByNoIn(List<Long> ids) {
        return repository.values().stream().filter(d -> ids.contains(d.getNo())).collect(Collectors.toList());
    }

    @Override
    public List<Dictionary> findBySource(DictionarySource source) {
        return null;
    }

    @Override
    public List<Dictionary> findByChatUser(ChatUser chatUser) {
        return null;
    }

    private Long maxNo() {
        final OptionalLong max = repository.keySet().stream().mapToLong(l -> l).max();

        if(max.isPresent())
            return max.getAsLong() + 1l;
        return 1l;
    }
}
