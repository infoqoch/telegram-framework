package infoqoch.telegram.framework.update.resolver.custom;

import infoqoch.telegram.framework.update.resolver.param.UpdateRequestParam;

import java.util.List;

public interface CustomUpdateRequestParamRegister {
    List<UpdateRequestParam> paramRegister();
}
