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
        MarkdownStringBuilder msb = new MarkdownStringBuilder();
        final List<Dictionary> dictionaries = (List<Dictionary>) target;
        for (Dictionary dictionary : dictionaries) {
            msb.append(dictionary.toMarkdown());
        }
        return new UpdateResponse(SendType.MESSAGE, msb);
    }
}
