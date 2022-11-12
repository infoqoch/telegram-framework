package infoqoch.telegram.framework.update.dispatcher.signiture.returns;

import infoqoch.telegram.framework.update.UpdateRequestMapper;

import java.time.LocalDateTime;

public class ReturnHandler {
    @UpdateRequestMapper({"help", "*"})
    public String help(){
        return "I am going to help you!";
    }

    @UpdateRequestMapper("hello")
    public LocalDateTime hello(){
        return LocalDateTime.now();
    }
}
