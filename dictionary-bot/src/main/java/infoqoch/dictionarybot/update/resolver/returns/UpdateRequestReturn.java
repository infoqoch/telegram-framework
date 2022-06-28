package infoqoch.dictionarybot.update.resolver.returns;

import infoqoch.dictionarybot.update.response.UpdateResponse;

public interface UpdateRequestReturn {
    boolean support(Object target);

    UpdateResponse resolve(Object target);
}
