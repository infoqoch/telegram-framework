package infoqoch.telegram.framework.update.resolver.customreturns;

import infoqoch.telegram.framework.update.resolver.custom.CustomUpdateRequestReturnRegister;
import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturn;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Configuration
@Component
public class SampleCustomUpdateRequestReturnRegister implements CustomUpdateRequestReturnRegister {

    @Override
    public List<UpdateRequestReturn> returnRegister() {
        return Arrays.asList(new SampleUpdateRequestReturn());
    }
}
