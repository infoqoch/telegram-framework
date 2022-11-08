package infoqoch.telegram.framework.update.dispatcher.mapper.voids;

import infoqoch.telegram.framework.update.UpdateRequestMethodMapper;

public class VoidsHandler {
    @UpdateRequestMethodMapper("*")
    public String any(){
        return "any!";
    }

    @UpdateRequestMethodMapper("voids")
    public void voids(){

    }
}
