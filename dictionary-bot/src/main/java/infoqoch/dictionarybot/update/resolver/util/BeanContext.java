package infoqoch.dictionarybot.update.resolver.util;

import org.springframework.beans.BeansException;

public interface BeanContext {
    <T> T getBean(Class<T> requiredType) throws BeansException;
}
