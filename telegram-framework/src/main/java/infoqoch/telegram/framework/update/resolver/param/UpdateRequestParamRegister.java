package infoqoch.telegram.framework.update.resolver.param;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UpdateRequestParamRegister {
    List<UpdateRequestParam> paramResolvers = new ArrayList<>();

    public void add(UpdateRequestParam updateRequestParam) {
        paramResolvers.add(updateRequestParam);
    }

    public UpdateRequestParam findSupportedResolverBy(Parameter parameter) {
        Optional<UpdateRequestParam> resolver = paramResolvers.stream().filter(r -> r.support(parameter)).findAny();

        if(resolver.isEmpty())  throw new IllegalArgumentException("can not resolve the parameter type. parameter type : " + parameter.getType());

        return resolver.get();
    }
}
