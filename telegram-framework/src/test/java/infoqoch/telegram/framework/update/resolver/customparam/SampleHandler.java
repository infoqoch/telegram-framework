package infoqoch.telegram.framework.update.resolver.customparam;

import infoqoch.telegram.framework.update.UpdateRequestMethodMapper;
import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.request.body.UpdateDocument;

public class SampleHandler{
    @UpdateRequestMethodMapper("*")
    public String any(){
        return "any";
    }

    @UpdateRequestMethodMapper("hi")
    String updateRequestMethod(UpdateRequest updateRequest, UpdateDocument updateDocument, Sample sample){
        return "good";
    }
}