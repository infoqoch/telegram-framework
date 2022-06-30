package infoqoch.dictionarybot.update.controller.resolver.returns;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class DictionariesUpdateRequestReturn implements UpdateRequestReturn {

    @Override
    public boolean support(Object target) {
        return (target instanceof List
                && ((List) target).stream().noneMatch((o -> !(o instanceof Dictionary))));
    }

    @Override
    public boolean support(Method target) {
        if (target.getReturnType() == List.class){
            if(target.getGenericReturnType().getTypeName().contains(type().getPackageName())){
                return true;
            }
        }
        return false;
    }

    private List<Dictionary> type = new ArrayList<>();

    private Class<?> type(){
        try {
            Field stringListField = DictionariesUpdateRequestReturn.class.getDeclaredField("type");
            ParameterizedType stringListType = (ParameterizedType) stringListField.getGenericType();
            return (Class<?>) stringListType.getActualTypeArguments()[0];
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public UpdateResponse resolve(Object target) {
        MarkdownStringBuilder msb = toMarkdown((List<Dictionary>) target);
        if(msb.size()==0) return new UpdateResponse(SendType.MESSAGE, new MarkdownStringBuilder("검색결과를 찾을 수 없습니다."));

        return new UpdateResponse(SendType.MESSAGE, msb);
    }

    private MarkdownStringBuilder toMarkdown(List<Dictionary> target) {
        MarkdownStringBuilder msb = new MarkdownStringBuilder();
        final List<Dictionary> dictionaries = target;
        for (Dictionary dictionary : dictionaries) {
            msb.append(dictionary.toMarkdown()).lineSeparator();
        }
        return msb;
    }
}
