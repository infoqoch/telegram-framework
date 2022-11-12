package infoqoch.telegram.framework.update.dispatcher.mapper.dup;

import infoqoch.telegram.framework.update.UpdateRequestMapper;

public class DupStarSampleHandler {
    @UpdateRequestMapper({"help", "*"})
    public String help(){
        return "I am going to help you!";
    }

    @UpdateRequestMapper("help")
    public String help2(){
        return "good!!";
    }
}
