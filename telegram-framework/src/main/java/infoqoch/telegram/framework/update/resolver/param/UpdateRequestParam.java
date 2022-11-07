package infoqoch.telegram.framework.update.resolver.param;

import infoqoch.telegram.framework.update.request.UpdateRequest;

import java.lang.reflect.Parameter;

public interface UpdateRequestParam {
    boolean support(Parameter target);

    Object resolve(UpdateRequest request);
}
