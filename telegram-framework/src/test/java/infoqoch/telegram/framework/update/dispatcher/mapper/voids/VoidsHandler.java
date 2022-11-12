package infoqoch.telegram.framework.update.dispatcher.mapper.voids;

import infoqoch.telegram.framework.update.UpdateRequestMapper;

public class VoidsHandler {
    @UpdateRequestMapper("*")
    public String any(){
        return "any!";
    }

    @UpdateRequestMapper("voids")
    public void voids(){

    }
}
