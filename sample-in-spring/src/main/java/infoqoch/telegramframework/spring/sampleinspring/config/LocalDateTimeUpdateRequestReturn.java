package infoqoch.telegramframework.spring.sampleinspring.config;

import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturn;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

public class LocalDateTimeUpdateRequestReturn implements UpdateRequestReturn {

    @Override
    public boolean support(Object target) {
        return target.getClass() == LocalDateTime.class;
    }

    @Override
    public boolean support(Method target) {
        return target.getReturnType() == LocalDateTime.class;
    }

    @Override
    public UpdateResponse resolve(Object target) {
        final LocalDateTime dt = (LocalDateTime) target;

        final MarkdownStringBuilder msb = new MarkdownStringBuilder()
                .plain(String.valueOf(dt.getYear())).plain("년 ")
                .plain(String.valueOf(dt.getMonthValue())).plain("월 ")
                .plain(String.valueOf(dt.getDayOfMonth())).plain("일 ")
                .plain("입니다.").lineSeparator();
        return UpdateResponse.message(msb);
    }
}
