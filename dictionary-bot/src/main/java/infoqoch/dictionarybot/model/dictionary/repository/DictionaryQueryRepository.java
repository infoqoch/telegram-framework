package infoqoch.dictionarybot.model.dictionary.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.model.user.QChatUser;
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
    public List<Dictionary> findByContentWord(ChatUser chatUser, String value){
        return findDictionary(dictionary.content.word.eq(value), findWithChatUser(chatUser));
    }

    public List<Dictionary> findByContentWordStartsWith(ChatUser chatUser, String value){
        return findDictionary(dictionary.content.word.startsWith(value), findWithChatUser(chatUser));
    }

    public List<Dictionary> findByContentWordEndsWith(ChatUser chatUser, String value){
        return findDictionary(dictionary.content.word.endsWith(value), findWithChatUser(chatUser));
    }

    public List<Dictionary> findByContentWordContains(ChatUser chatUser, String value){
        return findDictionary(dictionary.content.word.contains(value), findWithChatUser(chatUser));
    }

    // definition
    public List<Dictionary> findByContentDefinition(ChatUser chatUser, String value){
        return findDictionary(dictionary.content.definition.eq(value));
    }

    public List<Dictionary> findByContentDefinitionStartsWith(ChatUser chatUser, String value){
        return findDictionary(dictionary.content.definition.startsWith(value));
    }

    public List<Dictionary> findByContentDefinitionEndsWith(ChatUser chatUser, String value){
        return findDictionary(dictionary.content.definition.endsWith(value));
    }

    public List<Dictionary> findByContentDefinitionContains(ChatUser chatUser, String value){
        return findDictionary(dictionary.content.definition.contains(value));
    }

    // sentence
    public List<Dictionary> findByContentSentence(ChatUser chatUser, String value){
        return findDictionary(dictionary.content.sentence.eq(value));
    }

    public List<Dictionary> findByContentSentenceStartsWith(ChatUser chatUser, String value){
        return findDictionary(dictionary.content.sentence.startsWith(value));
    }

    public List<Dictionary> findByContentSentenceEndsWith(ChatUser chatUser, String value){
        return findDictionary(dictionary.content.sentence.endsWith(value));
    }

    public List<Dictionary> findByContentSentenceContains(ChatUser chatUser, String value){
        return findDictionary(dictionary.content.sentence.contains(value));
    }

    // 공통 메서드
    private List<Dictionary> findDictionary(BooleanExpression expression, BooleanExpression ... expressions) {
        return queryFactory
                .selectFrom(dictionary)
                .join(dictionary.chatUser, QChatUser.chatUser)
                .where(expression).where(expressions)
                .fetch();
    }
    /*
    * 나의 것만 검색
    * 남의 것을 검색....내 것을 숨긴 것은 다른 사람 것은 검색 안함 but 나의 것은 검색에 포함해야함.
    */
    private BooleanExpression findWithChatUser(ChatUser chatUser) {
        if(!chatUser.isLookupPublicData()) return dictionary.chatUser.eq(chatUser);
        return dictionary.chatUser.eq(chatUser).or(QChatUser.chatUser.openDataPublic.eq(true));

    }

}

