package infoqoch.telegram.framework.update.resolver.customreturns;

import infoqoch.telegram.framework.update.UpdateRequestMethodMapper;

public class AnyHandler {
    @UpdateRequestMethodMapper("*")
    public String any(){
        return "any";
    }
}
