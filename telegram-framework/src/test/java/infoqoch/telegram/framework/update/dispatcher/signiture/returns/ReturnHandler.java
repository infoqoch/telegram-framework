package infoqoch.telegram.framework.update.dispatcher.signiture.returns;

import infoqoch.telegram.framework.update.UpdateRequestMethodMapper;

import java.time.LocalDateTime;

public class ReturnHandler {
    @UpdateRequestMethodMapper({"help", "*"})
    public String help(){
        return "I am going to help you!";
    }

    @UpdateRequestMethodMapper("hello")
    public LocalDateTime hello(){
        return LocalDateTime.now();
    }
}
