package infoqoch.dictionarybot.mock.update;

import infoqoch.dictionarybot.update.controller.resolver.param.UpdateRequestParam;
import infoqoch.dictionarybot.update.request.UpdateRequest;

import java.lang.reflect.Parameter;

public class ThrowRuntimeExceptionRequestParam implements UpdateRequestParam {
    @Override
    public boolean support(Parameter target) {
        return target.getType()== RuntimeException.class;
    }

    @Override
    public Object resolve(UpdateRequest request) {
        if(true) throw new RuntimeException("강제 에러 발생!!!");
        return null;
    }
}
