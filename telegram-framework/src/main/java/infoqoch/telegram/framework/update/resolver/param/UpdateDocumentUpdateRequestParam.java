package infoqoch.telegram.framework.update.resolver.param;

import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.request.body.UpdateDocument;

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
