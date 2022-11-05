package infoqoch.dictionarybot.config;

import infoqoch.dictionarybot.controller.resolver.DictionariesUpdateRequestReturn;
import infoqoch.dictionarybot.controller.resolver.DictionaryUpdateRequestReturn;
import infoqoch.dictionarybot.controller.resolver.UpdateChatUserRequestParam;
import infoqoch.dictionarybot.model.user.ChatUserRepository;
import infoqoch.telegram.framework.update.resolver.custom.CustomUpdateRequestParamRegister;
import infoqoch.telegram.framework.update.resolver.custom.CustomUpdateRequestReturnRegister;
import infoqoch.telegram.framework.update.resolver.param.UpdateRequestParam;
import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class CustomUpdateResolverConfig implements CustomUpdateRequestParamRegister, CustomUpdateRequestReturnRegister {
    private final ChatUserRepository chatUserRepository;

    @Autowired
    public CustomUpdateResolverConfig(ChatUserRepository chatUserRepository) {
        log.info("CustomUpdateResolverConfig called!");
        this.chatUserRepository = chatUserRepository;
    }

    @Override
    public List<UpdateRequestParam> paramRegister() {
        return Arrays.asList(new UpdateChatUserRequestParam(chatUserRepository));
    }

    @Override
    public List<UpdateRequestReturn> returnRegister() {
        return Arrays.asList(new DictionariesUpdateRequestReturn(), new DictionaryUpdateRequestReturn());
    }
}