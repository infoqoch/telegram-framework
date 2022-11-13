package infoqoch.telegramframework.spring.sampleinspring.config;

import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.resolver.param.UpdateRequestParam;
import infoqoch.telegramframework.spring.sampleinspring.user.User;
import infoqoch.telegramframework.spring.sampleinspring.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Parameter;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserUpdateRequestParam implements UpdateRequestParam {
    private final UserRepository userRepository;

    static {
        log.info("UserUpdateRequestParam called!!");
    }

    @Override
    public boolean support(Parameter target) {
        return target.getType() == User.class;
    }

    @Override
    public Object resolve(UpdateRequest request) {
        final Optional<User> user = userRepository.findByChatId(request.chatId());
        if(user.isPresent()){
            return user.get();
        }
        final User saved = userRepository.save(new User(request.chatId(), request.toChat().getChat().getFirstName()));
        log.info("user saved : {} ", saved);
        return saved;
    }
}
