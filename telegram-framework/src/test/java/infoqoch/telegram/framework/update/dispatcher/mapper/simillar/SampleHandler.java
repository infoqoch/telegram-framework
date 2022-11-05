package infoqoch.telegram.framework.update.dispatcher.mapper.simillar;

import infoqoch.telegram.framework.update.UpdateRequestMethodMapper;

public class SampleHandler {
    @UpdateRequestMethodMapper("*")
    public String any(){
        return "any!";
    }

    @UpdateRequestMethodMapper("help")
    public String help(){
        return "help!";
    }

    @UpdateRequestMethodMapper("help me")
    public String helpMe(){
        return "help ME!!";
    }
}
