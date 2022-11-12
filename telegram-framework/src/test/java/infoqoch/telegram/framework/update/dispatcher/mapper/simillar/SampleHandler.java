package infoqoch.telegram.framework.update.dispatcher.mapper.simillar;

import infoqoch.telegram.framework.update.UpdateRequestMapper;

public class SampleHandler {
    @UpdateRequestMapper("*")
    public String any(){
        return "any!";
    }

    @UpdateRequestMapper("help")
    public String help(){
        return "help!";
    }

    @UpdateRequestMapper("help me")
    public String helpMe(){
        return "help ME!!";
    }
}
