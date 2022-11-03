package infoqoch.dictionarybot.mock.update;

import infoqoch.telegram.framework.update.resolver.bean.BeanContext;
import lombok.Setter;
import org.springframework.beans.BeansException;

import java.util.Map;

public class FakeMapBeanContext implements BeanContext {
    @Setter
    private Map<Class<?>, Object> context;

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return (T) context.get(requiredType);
    }


}
