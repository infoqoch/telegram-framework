package infoqoch.telegram.framework.update.dispatcher.star;

import infoqoch.telegram.framework.update.UpdateRequestMethodMapper;

public class NoStarSampleHandler {
    @UpdateRequestMethodMapper({"help!"})
    public String help(){
        return "I am going to help you!";
    }

    @UpdateRequestMethodMapper("good")
    public String good(){
        return "good!!";
    }
}
