package infoqoch.dictionarybot.update.resolver.returns;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;

public class DictionaryUpdateRequestReturn implements UpdateRequestReturn {
    @Override
    public boolean support(Object target) {
        return target instanceof Dictionary;
    }

    @Override
    public UpdateResponse resolve(Object target) {
        final Dictionary dictionary = (Dictionary) target;
        return new UpdateResponse(SendType.MESSAGE, dictionary.toMarkdown());
    }
}
