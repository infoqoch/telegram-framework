package infoqoch.dictionarybot.update.resolver.custom;

import infoqoch.dictionarybot.update.resolver.param.UpdateRequestParam;
import infoqoch.dictionarybot.update.resolver.returns.UpdateRequestReturn;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class EmptyCustomUpdateRequestParmAndReturn implements CustomUpdateRequestParam, CustomUpdateRequestReturn{

    @Override
    public List<UpdateRequestParam> addUpdateRequestParam() {
        return Collections.emptyList();
    }

    @Override
    public List<UpdateRequestReturn> addUpdateRequestReturn() {
        return Collections.emptyList();
    }
}
