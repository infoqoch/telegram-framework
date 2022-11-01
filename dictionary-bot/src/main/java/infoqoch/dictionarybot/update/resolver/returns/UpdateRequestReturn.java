package infoqoch.dictionarybot.update.resolver.returns;

import infoqoch.dictionarybot.update.response.UpdateResponse;

import java.lang.reflect.Method;

public interface UpdateRequestReturn {
    boolean support(Method target);

    boolean support(Object target);

    UpdateResponse resolve(Object target);
}
