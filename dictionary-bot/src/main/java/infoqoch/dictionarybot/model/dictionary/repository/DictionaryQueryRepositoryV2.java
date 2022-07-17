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

    public List<Dictionary> lookup(int limit, int offset, String target, FindBy firstFindBy, FindBy ... findBy) {
        final List<FindBy> findByList = gatheringFindBy(firstFindBy, findBy);
        final BooleanExpression[] expressions = lookupConditionOrderByPriority(target, findByList);
        return unionDictionary(limit, offset, expressions);
    }

    private List<FindBy> gatheringFindBy(FindBy firstFindBy, FindBy[] findBys) {
        List<FindBy> result = new ArrayList<>();
        result.add(firstFindBy);

        if(findBys==null||findBys.length==0) return result;

        result.addAll(List.of(findBys));
        return result;
    }

    private BooleanExpression[] lookupConditionOrderByPriority(String value, List<FindBy> findBy) {
        BooleanExpression[] expressions = new BooleanExpression[3];
        expressions[0] = eq(value, findBy);
        expressions[1] = startsWith(value, findBy);
        expressions[2] = contains(value, findBy);
        return expressions;
    }

    private BooleanExpression eq(String value, List<FindBy> findBy) {
        BooleanExpression result = findBy.get(0).path.eq(value);
        for(int i=1; i<findBy.size(); i++){
            result = result.or(findBy.get(i).path.eq(value));
        }
        return result;
    }

    private BooleanExpression startsWith(String value, List<FindBy> findBy) {
        BooleanExpression result = findBy.get(0).path.startsWith(value);
        for(int i=1; i<findBy.size(); i++){
            result = result.or(findBy.get(i).path.startsWith(value));
        }
        return result;
    }

    private BooleanExpression contains(String value, List<FindBy> findBy) {
        BooleanExpression result = findBy.get(0).path.contains(value);
        for(int i=1; i<findBy.size(); i++){
            result = result.or(findBy.get(i).path.contains(value));
        }
        return result;
    }

    public List<Dictionary> unionDictionary(long limit, long offset, BooleanExpression...args) {
        long rLimit = limit;
        long rOffset = offset;

        List<Dictionary> result = new ArrayList<>();
        for(int i=0; i<args.length; i++){
            BooleanExpression[] expressions = generateExpressionGradually(args, i);

            final List<Dictionary> dictionaries = findDictionary(rLimit, rOffset, expressions);
            result.addAll(dictionaries);

            rLimit = rLimit - dictionaries.size();
            if(rLimit<=0) return result;

            rOffset = calculateOffset(rOffset, expressions);
        }
        return result;
    }

    private BooleanExpression[] generateExpressionGradually(BooleanExpression[] args, int lastOf) {
        BooleanExpression[] expressions = new BooleanExpression[lastOf +1];
        for (int j=0; j<expressions.length; j++) {
            expressions[j] = makeLastNegative(args[j], expressions.length, j);
        }
        return expressions;
    }

    private BooleanExpression makeLastNegative(BooleanExpression arg, int length, int index) {
        if(index == length -1)
            return arg;
        return arg.not();

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