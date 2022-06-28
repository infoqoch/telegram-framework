package infoqoch.dictionarybot.update.resolver.returns;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

public class DictionaryUpdateRequestMethodParameterResolver implements UpdateRequestMethodParameterResolver {
    @Override
    public boolean support(Object target) {
        return target instanceof Dictionary;
    }

    @Override
    public MarkdownStringBuilder resolve(Object target) {
        final Dictionary dictionary = (Dictionary) target;
        return dictionary.toMarkdown();
    }
}
