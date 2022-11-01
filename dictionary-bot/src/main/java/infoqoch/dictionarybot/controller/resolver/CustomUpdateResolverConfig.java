package infoqoch.dictionarybot.controller.resolver;

import infoqoch.dictionarybot.model.user.ChatUserRepository;
import infoqoch.dictionarybot.update.resolver.custom.CustomUpdateRequestParam;
import infoqoch.dictionarybot.update.resolver.custom.CustomUpdateRequestReturn;
import infoqoch.dictionarybot.update.resolver.param.UpdateRequestParam;
import infoqoch.dictionarybot.update.resolver.returns.UpdateRequestReturn;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;

@Primary
@Configuration
@RequiredArgsConstructor
public class CustomUpdateResolverConfig implements CustomUpdateRequestParam, CustomUpdateRequestReturn {
    private final ChatUserRepository chatUserRepository;

    @Override
    public List<UpdateRequestParam> addUpdateRequestParam() {
        List<UpdateRequestParam> updateRequestParams = new ArrayList<>();
        updateRequestParams.add(new UpdateChatUserRequestParam(chatUserRepository));
        return updateRequestParams;
    }

    @Override
    public List<UpdateRequestReturn> addUpdateRequestReturn() {
        List<UpdateRequestReturn> returnResolvers = new ArrayList<>();
        returnResolvers.add(new DictionariesUpdateRequestReturn());
        returnResolvers.add(new DictionaryUpdateRequestReturn());
        return returnResolvers;
    }
}
