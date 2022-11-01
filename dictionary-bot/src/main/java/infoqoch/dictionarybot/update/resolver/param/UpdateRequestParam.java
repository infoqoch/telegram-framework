package infoqoch.dictionarybot.update.resolver.param;

import infoqoch.dictionarybot.update.request.UpdateRequest;

import java.lang.reflect.Parameter;

public interface UpdateRequestParam {
    boolean support(Parameter target);

    Object resolve(UpdateRequest request);
}
