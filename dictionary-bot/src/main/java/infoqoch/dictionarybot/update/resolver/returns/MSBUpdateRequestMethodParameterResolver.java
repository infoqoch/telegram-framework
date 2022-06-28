package infoqoch.dictionarybot.update.resolver.returns;

import infoqoch.telegrambot.util.MarkdownStringBuilder;

public class MSBUpdateRequestMethodParameterResolver implements UpdateRequestMethodParameterResolver {
    @Override
    public boolean support(Object target) {
        return target.getClass() == MarkdownStringBuilder.class;
    }


    @Override
    public MarkdownStringBuilder resolve(Object target) {
        return (MarkdownStringBuilder) target;
    }
}
