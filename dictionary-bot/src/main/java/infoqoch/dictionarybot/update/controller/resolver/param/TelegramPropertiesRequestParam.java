package infoqoch.dictionarybot.update.controller.resolver.param;

import infoqoch.dictionarybot.system.properties.TelegramProperties;
import infoqoch.dictionarybot.update.request.UpdateRequest;

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
