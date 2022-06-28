package infoqoch.dictionarybot.update.resolver.returns;

import infoqoch.dictionarybot.update.response.UpdateResponse;

public class UpdateResponseUpdateRequestReturn implements UpdateRequestReturn {
    @Override
    public boolean support(Object target) {
        return (target instanceof UpdateResponse);
    }

    @Override
    public UpdateResponse resolve(Object target) {
        return (UpdateResponse) target;
    }
}
