package infoqoch.telegram.framework.update.resolver.param;

import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.request.body.UpdateChat;

import java.lang.reflect.Parameter;

public class UpdateChatUpdateRequestParam implements UpdateRequestParam {
    @Override
    public boolean support(Parameter target) {
        return target.getType() == UpdateChat.class;
    }

    @Override
    public Object resolve(UpdateRequest request) {
        return request.findBodyByType(UpdateChat.class);
    }
}
