package infoqoch.telegram.framework.update.dispatcher.signiture.param;

import infoqoch.telegram.framework.update.UpdateRequestMapper;

import java.time.LocalDateTime;

public class ParamHandler {
    @UpdateRequestMapper({"help", "*"})
    public String help(){
        return "I am going to help you!";
    }

    @UpdateRequestMapper("hello")
    public String hello(LocalDateTime localDateTime){
        return "hi!";
    }
}
