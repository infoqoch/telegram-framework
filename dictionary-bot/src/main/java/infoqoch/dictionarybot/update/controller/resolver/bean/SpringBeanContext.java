package infoqoch.dictionarybot.update.controller.resolver.bean;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

@AllArgsConstructor
public class SpringBeanContext implements BeanContext{
    private final ApplicationContext context;

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return context.getBean(requiredType);
    }
}
