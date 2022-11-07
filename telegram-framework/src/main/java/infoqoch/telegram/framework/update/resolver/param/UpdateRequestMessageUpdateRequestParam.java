package infoqoch.telegram.framework.update.resolver.param;

import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.request.UpdateRequestCommandAndValue;

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
