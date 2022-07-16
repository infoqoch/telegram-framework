package infoqoch.dictionarybot.update.controller.resolver.returns;

import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

import java.lang.reflect.Method;

public class StringUpdateRequestReturn implements UpdateRequestReturn {
    @Override
    public boolean support(Object target) {
        return target.getClass() == String.class;
    }

    @Override
    public boolean support(Method target) {
        return target.getReturnType() == String.class;
    }

    @Override
    public UpdateResponse resolve(Object target) {
        return UpdateResponse.message(new MarkdownStringBuilder().plain((String) target));
    }
}
