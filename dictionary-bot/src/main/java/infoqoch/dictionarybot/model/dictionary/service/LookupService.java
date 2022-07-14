package infoqoch.dictionarybot.model.dictionary.service;


import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.repository.DictionaryQueryRepository;
import infoqoch.dictionarybot.model.user.ChatUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LookupService {
    private final DictionaryQueryRepository repository;

    public List<Dictionary> word(ChatUser chatUser, String value, int limit, int offset) {
        checkLimit(limit);

        Set<Dictionary> result = new LinkedHashSet<>();

        result.addAll(repository.findByContentWord(chatUser, value));
        if(result.size()>=(limit+offset)) return setToList(result, limit, offset);

        result.addAll(repository.findByContentWordStartsWith(chatUser, value));
        if(result.size()>=(limit+offset)) return setToList(result, limit, offset);

        result.addAll(repository.findByContentWordContains(chatUser, value));
        if(result.size()>=(limit+offset)) return setToList(result, limit, offset);

        return result.stream().collect(Collectors.toList());
    }

    public List<Dictionary> sentence(ChatUser chatUser, String value, int limit, int offset) {
        checkLimit(limit);

        Set<Dictionary> result = new LinkedHashSet<>();

        result.addAll(repository.findByContentSentence(chatUser, value));
        if(result.size()>=(limit+offset)) return setToList(result, limit, offset);

        result.addAll(repository.findByContentSentenceStartsWith(chatUser, value));
        if(result.size()>=(limit+offset)) return setToList(result, limit, offset);

        result.addAll(repository.findByContentSentenceContains(chatUser, value));
        if(result.size()>=(limit+offset)) return setToList(result, limit, offset);

        return result.stream().collect(Collectors.toList());
    }

    public List<Dictionary> definition(ChatUser chatUser, String value, int limit, int offset) {
        checkLimit(limit);

        Set<Dictionary> result = new LinkedHashSet<>();

        result.addAll(repository.findByContentDefinition(chatUser, value));
        if(result.size()>=(limit+offset)) return setToList(result, limit, offset);

        result.addAll(repository.findByContentDefinitionStartsWith(chatUser, value));
        if(result.size()>=(limit+offset)) return setToList(result, limit, offset);

        result.addAll(repository.findByContentDefinitionContains(chatUser, value));
        if(result.size()>=(limit+offset)) return setToList(result, limit, offset);

        return result.stream().collect(Collectors.toList());
    }

    private List<Dictionary> setToList(Set<Dictionary> result, int limit, int offset) {
        return result.stream().collect(Collectors.toList()).subList(offset, offset+limit);
    }

    private void checkLimit(int limit) {
        if(limit ==0) throw new IllegalArgumentException("limit 은 0이 될 수 없습니다.");
    }

    public Optional<Dictionary> getRandom() {
        return repository.getRandom();
    }

    public Optional<Dictionary> getRandom(ChatUser chatUser) {
        return repository.getRandom(chatUser);
    }
}
