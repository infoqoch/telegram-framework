package infoqoch.telegram.framework.update.resolver.returns;

import infoqoch.telegram.framework.update.response.UpdateResponse;

import java.lang.reflect.Method;

public class VoidUpdateRequestReturn implements UpdateRequestReturn {
    @Override
    public boolean support(Object target) {
        return target == null;
    }

    @Override
    public boolean support(Method target) {
        return (target.getReturnType() == void.class);
    }

    @Override
    public UpdateResponse resolve(Object target) {
        return UpdateResponse.voids();
    }
}
