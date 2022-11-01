package infoqoch.dictionarybot.update.resolver.param;

import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.request.UpdateRequestCommandAndValue;

import java.lang.reflect.Parameter;

public class UpdateRequestMessageUpdateRequestParam implements UpdateRequestParam {
    @Override
    public boolean support(Parameter target) {
        return target.getType()== UpdateRequestCommandAndValue.class;
    }

    @Override
    public Object resolve(UpdateRequest request) {
        return request.updateRequestCommandAndValue();
    }
}
