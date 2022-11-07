package infoqoch.telegram.framework.update.resolver.returns;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UpdateRequestReturnRegister {
    List<UpdateRequestReturn> requestReturns = new ArrayList<>();

    public void add(UpdateRequestReturn updateRequestReturn) {
        requestReturns.add(updateRequestReturn);
    }

    public Optional<UpdateRequestReturn> support(Method method) {
        return requestReturns.stream().filter(r -> r.support(method)).findAny();
    }

    public Optional<UpdateRequestReturn> support(Object target) {
        return requestReturns.stream().filter(r -> r.support(target)).findAny();
    }
}
