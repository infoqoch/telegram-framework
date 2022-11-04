package infoqoch.telegram.framework.update.dispatcher.dup;

import infoqoch.telegram.framework.update.UpdateRequestMethodMapper;

public class DupStarSampleHandler {
    @UpdateRequestMethodMapper({"help", "*"})
    public String help(){
        return "I am going to help you!";
    }

    @UpdateRequestMethodMapper("help")
    public String help2(){
        return "good!!";
    }
}
