package infoqoch.telegram.framework.update.dispatcher.signiture.param;

import infoqoch.telegram.framework.update.UpdateRequestMethodMapper;

import java.time.LocalDateTime;

public class ParamHandler {
    @UpdateRequestMethodMapper({"help", "*"})
    public String help(){
        return "I am going to help you!";
    }

    @UpdateRequestMethodMapper("hello")
    public String hello(LocalDateTime localDateTime){
        return "hi!";
    }
}
