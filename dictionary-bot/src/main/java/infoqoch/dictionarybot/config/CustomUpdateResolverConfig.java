package infoqoch.dictionarybot.config;

import infoqoch.dictionarybot.controller.resolver.DictionariesUpdateRequestReturn;
import infoqoch.dictionarybot.controller.resolver.DictionaryUpdateRequestReturn;
import infoqoch.dictionarybot.controller.resolver.UpdateChatUserRequestParam;
import infoqoch.dictionarybot.model.user.ChatUserRepository;
import infoqoch.telegram.framework.update.resolver.param.UpdateRequestParamRegister;
import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturnRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CustomUpdateResolverConfig {
    private final ChatUserRepository chatUserRepository;

    @Autowired
    public CustomUpdateResolverConfig(ChatUserRepository chatUserRepository) {
        log.info("CustomUpdateResolverConfig called!");
        this.chatUserRepository = chatUserRepository;
    }

    @Autowired
    public void addUpdateRequestParam(UpdateRequestParamRegister register) {
        log.info("hihi!");
        register.add(new UpdateChatUserRequestParam(chatUserRepository));
    }

    @Autowired
    public void addUpdateRequestReturn(UpdateRequestReturnRegister register) {
        log.info("hihi!!");
        register.add(new DictionariesUpdateRequestReturn());
        register.add(new DictionaryUpdateRequestReturn());
    }

}