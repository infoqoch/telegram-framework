package infoqoch.dictionarybot.model.dictionary.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.user.QChatUser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static infoqoch.dictionarybot.model.dictionary.QDictionary.dictionary;


@Repository
public class DictionaryQueryRepositoryV2 {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public DictionaryQueryRepositoryV2(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public enum FindBy{
        WORD(dictionary.content.word)
        , DEFINITION(dictionary.content.definition)
        , SENTENCE(dictionary.content.sentence);

        private final StringPath path;

        FindBy(StringPath path) {
            this.path = path;
        }
    }

    public List<Dictionary> lookup(int limit, int offset, String target, FindBy findBy) {
        final BooleanExpression[] expressions = lookupConditionOrderByPriority(target, dictionary.content.word);
        return unionDictionary(limit, offset, expressions);
    }

    private BooleanExpression[] lookupConditionOrderByPriority(String value, StringPath path) {
        BooleanExpression[] expressions = new BooleanExpression[3];
        expressions[0] = path.eq(value);
        expressions[1] = path.startsWith(value);
        expressions[2] = path.contains(value);
        return expressions;
    }

    public List<Dictionary> unionDictionary(long limit, long offset, BooleanExpression...args) {
        long rLimit = limit;
        long rOffset = offset;

        List<Dictionary> result = new ArrayList<>();
        for(int i=0; i<args.length; i++){
            BooleanExpression[] expressions = generateExpressionGradually(i, args);

            final List<Dictionary> dictionaries = findDictionary(rLimit, rOffset, expressions);
            result.addAll(dictionaries);

            rLimit = rLimit - dictionaries.size();
            if(rLimit<=0) return result;

            rOffset = calculateOffset(rOffset, expressions);
        }
        return result;
    }

    private BooleanExpression[] generateExpressionGradually(int i, BooleanExpression[] args) {
        BooleanExpression[] expressions = new BooleanExpression[i +1];
        for (int j=0; j<expressions.length; j++) {
            expressions[j] = negativeExcludeLast(expressions, j, args);
        }
        return expressions;
    }

    private BooleanExpression negativeExcludeLast(BooleanExpression[] expressions, int j, BooleanExpression[] args) {
        if(j == expressions.length-1)
            return args[j];
        return args[j].not();

    }

    private List<Dictionary> findDictionary(long limit, long offset, BooleanExpression[] expressions) {
        return queryFactory
                .selectFrom(dictionary)
                .join(dictionary.chatUser, QChatUser.chatUser)
                .where(expressions)
                .limit(limit)
                .offset(offset)
                .fetch();
    }

    private long calculateOffset(long rOffset, BooleanExpression[] expressions) {
        final long count = countDictionary(expressions);
        rOffset = rOffset - count < 0 ? 0 : rOffset - count;
        return rOffset;
    }

    private long countDictionary(BooleanExpression[] expressions) {
        return queryFactory
                .select(dictionary.no.count())
                .from(dictionary)
                .join(dictionary.chatUser, QChatUser.chatUser)
                .where(expressions)
                .fetchOne();
    }

}

