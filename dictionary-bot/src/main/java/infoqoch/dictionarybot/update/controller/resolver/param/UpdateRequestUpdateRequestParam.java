package infoqoch.dictionarybot.update.controller.resolver.param;

import infoqoch.dictionarybot.update.request.UpdateRequest;

import java.lang.reflect.Parameter;

public class UpdateRequestUpdateRequestParam implements UpdateRequestParam {
    @Override
    public boolean support(Parameter target) {
        return target.getType()==UpdateRequest.class;
    }

    @Override
    public Object resolve(UpdateRequest request) {
        return request;
    }
}
