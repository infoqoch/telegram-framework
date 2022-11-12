package infoqoch.telegram.framework.update.resolver.customparam;

import infoqoch.telegram.framework.update.UpdateRequestMapper;
import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.request.body.UpdateDocument;

public class SampleHandler{
    @UpdateRequestMapper("*")
    public String any(){
        return "any";
    }

    @UpdateRequestMapper("hi")
    String updateRequestMethod(UpdateRequest updateRequest, UpdateDocument updateDocument, Sample sample){
        return "good";
    }
}