package infoqoch.telegram.framework.update.resolver.customparam;

import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.resolver.param.UpdateRequestParam;

import java.lang.reflect.Parameter;
import java.time.LocalDateTime;

public class SampleUpdateRequestParam implements UpdateRequestParam {
    @Override
    public boolean support(Parameter target) {
        return target.getType()== Sample.class;
    }

    @Override
    public Object resolve(UpdateRequest request) {
        return new Sample(request.updateId(), LocalDateTime.now());
    }
}
