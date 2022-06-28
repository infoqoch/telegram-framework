package infoqoch.dictionarybot.update.resolver.returns;

import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

public class StringUpdateRequestReturn implements UpdateRequestReturn {
    @Override
    public boolean support(Object target) {
        return target.getClass() == String.class;
    }

    @Override
    public UpdateResponse resolve(Object target) {
        return new UpdateResponse(SendType.MESSAGE, new MarkdownStringBuilder().plain((String) target));
    }
}
