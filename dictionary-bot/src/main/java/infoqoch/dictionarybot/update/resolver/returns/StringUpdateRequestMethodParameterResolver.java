package infoqoch.dictionarybot.update.resolver.returns;

import infoqoch.telegrambot.util.MarkdownStringBuilder;

public class StringUpdateRequestMethodParameterResolver implements UpdateRequestMethodParameterResolver {
    @Override
    public boolean support(Object target) {
        return target.getClass() == String.class;
    }

    @Override
    public MarkdownStringBuilder resolve(Object target) {
        return new MarkdownStringBuilder().plain((String) target);
    }
}
