package infoqoch.dictionarybot.config;

import com.p6spy.engine.spy.P6SpyOptions;
import infoqoch.dictionarybot.system.log.MyP6spyFormattingStrategy;

import javax.annotation.PostConstruct;

// @Configuration
public class LogConfig {
    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(MyP6spyFormattingStrategy.class.getName());
    }
}
