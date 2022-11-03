package infoqoch.telegram.framework.update.resolver.returns;

import infoqoch.telegram.framework.update.response.UpdateResponse;

import java.lang.reflect.Method;

public class UpdateResponseUpdateRequestReturn implements UpdateRequestReturn {
    @Override
    public boolean support(Object target) {
        return (target instanceof UpdateResponse);
    }

    @Override
    public boolean support(Method target) {
        return (target.getReturnType() == UpdateResponse.class);
    }

    @Override
    public UpdateResponse resolve(Object target) {
        return (UpdateResponse) target;
    }
}
