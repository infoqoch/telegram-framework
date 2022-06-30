package infoqoch.dictionarybot.update.controller.resolver.returns;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;

import java.lang.reflect.Method;

public class DictionaryUpdateRequestReturn implements UpdateRequestReturn {
    @Override
    public boolean support(Object target) {
        return target instanceof Dictionary;
    }

    @Override
    public boolean support(Method target) {
        return target.getReturnType() == Dictionary.class;
    }

    @Override
    public UpdateResponse resolve(Object target) {
        final Dictionary dictionary = (Dictionary) target;
        return new UpdateResponse(SendType.MESSAGE, dictionary.toMarkdown());
    }
}
