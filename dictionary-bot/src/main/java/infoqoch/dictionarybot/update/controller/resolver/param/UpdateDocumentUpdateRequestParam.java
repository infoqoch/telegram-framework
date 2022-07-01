package infoqoch.dictionarybot.update.controller.resolver.param;

import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.request.body.UpdateDocument;

import java.lang.reflect.Parameter;

public class UpdateDocumentUpdateRequestParam implements UpdateRequestParam {
    @Override
    public boolean support(Parameter target) {
        return target.getType() == UpdateDocument.class;
    }

    @Override
    public Object resolve(UpdateRequest request) {
        return request.findBodyByType(UpdateDocument.class);
    }
}
