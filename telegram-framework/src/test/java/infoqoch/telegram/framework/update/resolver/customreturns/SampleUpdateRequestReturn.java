package infoqoch.telegram.framework.update.resolver.customreturns;

import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturn;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

import java.lang.reflect.Method;

public class SampleUpdateRequestReturn implements UpdateRequestReturn {
    @Override
    public boolean support(Object target) {
        return target.getClass() == Sample.class;
    }

    @Override
    public boolean support(Method target) {
        return target.getReturnType() == Sample.class;
    }

    @Override
    public UpdateResponse resolve(Object target) {
        final Sample sample = (Sample) target;
        final MarkdownStringBuilder msb = new MarkdownStringBuilder()
                .bold("update id : [" + sample.getUpdateId() + "]").lineSeparator()
                .italic("date : ").italic((sample.getRegDt()).toString()).lineSeparator();
        return UpdateResponse.message(msb);
    }
}
