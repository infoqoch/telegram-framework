package infoqoch.telegram.framework.update.resolver.customparam;

import infoqoch.telegram.framework.update.resolver.custom.CustomUpdateRequestParamRegister;
import infoqoch.telegram.framework.update.resolver.param.UpdateRequestParam;

import java.util.Arrays;
import java.util.List;

public class SampleCustomUpdateRequestParamRegister implements CustomUpdateRequestParamRegister {
    @Override
    public List<UpdateRequestParam> paramRegister() {
        return Arrays.asList(new SampleUpdateRequestParam());
    }
}
