package infoqoch.telegram.framework.update.resolver.custom;

import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturn;

import java.util.List;

public interface CustomUpdateRequestReturn {
    List<UpdateRequestReturn> addUpdateRequestReturn();
}
