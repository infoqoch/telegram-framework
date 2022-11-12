package infoqoch.telegram.framework.update.dispatcher.mapper.star;

import infoqoch.telegram.framework.update.UpdateRequestMapper;

public class NoStarSampleHandler {
    @UpdateRequestMapper({"help!"})
    public String help(){
        return "I am going to help you!";
    }

    @UpdateRequestMapper("good")
    public String good(){
        return "good!!";
    }
}
