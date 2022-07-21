package infoqoch.dictionarybot.system.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class IgnoreLogFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event.getMessage().contains("ignore-logging")) {
            return FilterReply.DENY;
        } else {
            return FilterReply.ACCEPT;
        }
    }
}
