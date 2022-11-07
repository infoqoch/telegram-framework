package infoqoch.telegram.framework.update.resolver.param;

import infoqoch.telegram.framework.update.util.TelegramProperties;
import infoqoch.telegram.framework.update.request.UpdateRequest;

import java.lang.reflect.Parameter;

public class TelegramPropertiesRequestParam implements UpdateRequestParam {
    private final TelegramProperties telegramProperties;

    public TelegramPropertiesRequestParam(TelegramProperties telegramProperties) {
        this.telegramProperties = telegramProperties;

    }

    @Override
    public boolean support(Parameter target) {
        return target.getType() == TelegramProperties.class;
    }

    @Override
    public Object resolve(UpdateRequest request) {
        return telegramProperties;
    }
}
