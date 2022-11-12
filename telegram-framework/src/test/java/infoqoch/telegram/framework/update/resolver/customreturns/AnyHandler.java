package infoqoch.telegram.framework.update.resolver.customreturns;

import infoqoch.telegram.framework.update.UpdateRequestMapper;

public class AnyHandler {
    @UpdateRequestMapper("*")
    public String any(){
        return "any";
    }
}
