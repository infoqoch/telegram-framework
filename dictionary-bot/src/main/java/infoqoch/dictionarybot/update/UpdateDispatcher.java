package infoqoch.dictionarybot.update;

import infoqoch.dictionarybot.update.controller.resolver.UpdateRequestMethodResolver;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
public class UpdateDispatcher {
    private final List<UpdateRequestMethodResolver> methodResolvers;

    public UpdateResponse process(UpdateRequest update) {
        return methodResolvers.stream().filter(r -> r.support(update)).findAny().get().process(update);
    }
}