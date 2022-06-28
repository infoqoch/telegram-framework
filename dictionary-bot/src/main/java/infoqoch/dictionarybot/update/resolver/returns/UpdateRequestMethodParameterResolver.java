package infoqoch.dictionarybot.update.resolver.returns;

import infoqoch.telegrambot.util.MarkdownStringBuilder;

public interface UpdateRequestMethodParameterResolver {
    boolean support(Object target);

    MarkdownStringBuilder resolve(Object target);
}
