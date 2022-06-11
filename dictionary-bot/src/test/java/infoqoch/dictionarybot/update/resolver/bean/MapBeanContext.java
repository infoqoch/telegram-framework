package infoqoch.dictionarybot.update.resolver.bean;

import lombok.Setter;
import org.springframework.beans.BeansException;

import java.util.Map;

public class MapBeanContext implements BeanContext {
    @Setter
    private Map<Class<?>, Object> context;

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return (T) context.get(requiredType);
    }


}
