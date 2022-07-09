package infoqoch.dictionarybot.model.dictionary.repository;

import infoqoch.dictionarybot.model.dictionary.Dictionary;

import java.util.List;
import java.util.Optional;

/*
* TODO
* 고민되는 지점.
* 1) Jpa를 우회하고, 2) Fake인 MemoryDictionaryRepository를 사용하려 하며, 3) 대역의 입장에서 필요로 한 제한적인 메서드만을 구현하려면, 인터페이스가 따로 필요로 하다.
* 앞서의 제한적인 매서드를 제외한, 테스트에서도 jpa를 사용하는 메서드의 경우, 따로 구현하면 된다. DictionaryQueryRepository
* 그러니까 구조는 다음과 같은 형태가 된다.
* 1. class MemoryDictionaryRepository implements DictionaryRepository -> 테스트에서만 사용. JPA 없이 간단하게 처리.
* 2. interface DictionaryJpaRepository extends DictionaryRepository, JpaRepository -> 테스트, 운영 둘 다 사용. DictionaryRepository 인터페이스로 하여 테스트코드에서 MemoryRepository를 사용할 수 있음.
* 3. interface DictionaryQueryRepository extends JpaRepository -> 테스트, 운영 둘 다 사용. JPA를 사용.
*
* 여기서의 의문은 이런 다소 복잡한 인터페이스를 계속 활용하는 것이 맞냐는 점이다. 일단은 계속 사용하지만 고민 필요애 보임!
*/
public interface DictionaryRepository {
    Dictionary save(Dictionary dictionary);

    Optional<Dictionary> findByNo(Long dictionaryNo);

    List<Dictionary> findByContentWord(String word);

    List<Dictionary> findByContentSentence(String sentence);

    List<Dictionary> findByContentDefinition(String definition);

    List<Dictionary> findAll();
}
