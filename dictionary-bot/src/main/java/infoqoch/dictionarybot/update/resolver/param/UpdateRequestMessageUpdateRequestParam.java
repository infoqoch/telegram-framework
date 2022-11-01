package infoqoch.dictionarybot.update.resolver.param;

import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.request.UpdateRequestMessage;

import java.lang.reflect.Parameter;

public class UpdateRequestMessageUpdateRequestParam implements UpdateRequestParam {
    @Override
    public boolean support(Parameter target) {
        return target.getType()== UpdateRequestMessage.class;
    }

    @Override
    public Object resolve(UpdateRequest request) {
        return request.updateRequestMessage();
    }
}
