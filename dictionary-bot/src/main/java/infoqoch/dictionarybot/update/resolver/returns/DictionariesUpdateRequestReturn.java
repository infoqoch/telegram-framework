package infoqoch.dictionarybot.update.resolver.returns;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

import java.util.List;

public class DictionariesUpdateRequestReturn implements UpdateRequestReturn {
    @Override
    public boolean support(Object target) {
        return (target instanceof List
                && ((List) target).stream().noneMatch((o -> !(o instanceof Dictionary))));
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
            msb.append(dictionary.toMarkdown());
        }
        return msb;
    }
}
