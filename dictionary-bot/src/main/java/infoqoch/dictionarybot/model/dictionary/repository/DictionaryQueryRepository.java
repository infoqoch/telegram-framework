package infoqoch.dictionarybot.model.dictionary.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static infoqoch.dictionarybot.model.dictionary.QDictionary.dictionary;

@Repository
public class DictionaryQueryRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public DictionaryQueryRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    // word
    public List<Dictionary> findByContentWord(String value){
        return findDictionary(dictionary.content.word.eq(value));
    }

    public List<Dictionary> findByContentWordStartsWith(String value){
        return findDictionary(dictionary.content.word.startsWith(value));
    }

    public List<Dictionary> findByContentWordEndsWith(String value){
        return findDictionary(dictionary.content.word.endsWith(value));
    }
    
    public List<Dictionary> findByContentWordContains(String value){
        return findDictionary(dictionary.content.word.contains(value));
    }

    // definition
    public List<Dictionary> findByContentDefinition(String value){
        return findDictionary(dictionary.content.definition.eq(value));
    }

    public List<Dictionary> findByContentDefinitionStartsWith(String value){
        return findDictionary(dictionary.content.definition.startsWith(value));
    }

    public List<Dictionary> findByContentDefinitionEndsWith(String value){
        return findDictionary(dictionary.content.definition.endsWith(value));
    }

    public List<Dictionary> findByContentDefinitionContains(String value){
        return findDictionary(dictionary.content.definition.contains(value));
    }

    // sentence
    public List<Dictionary> findByContentSentence(String value){
        return findDictionary(dictionary.content.sentence.eq(value));
    }

    public List<Dictionary> findByContentSentenceStartsWith(String value){
        return findDictionary(dictionary.content.sentence.startsWith(value));
    }

    public List<Dictionary> findByContentSentenceEndsWith(String value){
        return findDictionary(dictionary.content.sentence.endsWith(value));
    }

    public List<Dictionary> findByContentSentenceContains(String value){
        return findDictionary(dictionary.content.sentence.contains(value));
    }

    // 공통 메서드
    private List<Dictionary> findDictionary(BooleanExpression where) {
        return queryFactory
                .selectFrom(dictionary)
                .where(where)
                .fetch();
    }
}

