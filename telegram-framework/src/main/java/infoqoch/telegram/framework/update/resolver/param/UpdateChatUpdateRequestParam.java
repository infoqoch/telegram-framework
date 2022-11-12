package infoqoch.telegram.framework.update.resolver.param;

import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.request.body.UpdateMessage;

import java.lang.reflect.Parameter;

public class UpdateChatUpdateRequestParam implements UpdateRequestParam {
    @Override
    public boolean support(Parameter target) {
        return target.getType() == UpdateMessage.class;
    }

    @Override
    public Object resolve(UpdateRequest request) {
        return request.findBodyByType(UpdateMessage.class);
    }
}
