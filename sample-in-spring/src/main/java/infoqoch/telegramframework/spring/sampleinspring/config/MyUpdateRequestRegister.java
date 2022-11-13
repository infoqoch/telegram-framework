package infoqoch.telegramframework.spring.sampleinspring.config;

import infoqoch.telegram.framework.update.resolver.custom.CustomUpdateRequestParamRegister;
import infoqoch.telegram.framework.update.resolver.custom.CustomUpdateRequestReturnRegister;
import infoqoch.telegram.framework.update.resolver.param.UpdateRequestParam;
import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturn;
import infoqoch.telegramframework.spring.sampleinspring.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// @UpdateRequestMapper 에 원하는 파라미터 리턴 타입을 설정한다.
// CustomUpdateRequestParamRegister, CustomUpdateRequestReturnRegister 를 구현하고 설정 파일 빈으로 등록한다.
@Configuration
@RequiredArgsConstructor
public class MyUpdateRequestRegister implements CustomUpdateRequestParamRegister, CustomUpdateRequestReturnRegister {
    private final UserRepository userRepository;

    @Override
    public List<UpdateRequestParam> paramRegister() {
        return Arrays.asList(new UserUpdateRequestParam(userRepository));
    }

    @Override
    public List<UpdateRequestReturn> returnRegister() {
        List<UpdateRequestReturn> result = new ArrayList<>();
        result.add(new LocalDateTimeUpdateRequestReturn());
        return result;
    }
}
