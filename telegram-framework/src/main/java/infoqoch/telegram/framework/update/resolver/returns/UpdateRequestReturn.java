package infoqoch.telegram.framework.update.resolver.returns;

import infoqoch.telegram.framework.update.response.UpdateResponse;

import java.lang.reflect.Method;

public interface UpdateRequestReturn {
    boolean support(Method target);

    boolean support(Object target);

    UpdateResponse resolve(Object target);
}
