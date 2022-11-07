package infoqoch.telegram.framework.update.resolver.bean;

import org.springframework.beans.BeansException;

public interface BeanContext {
    <T> T getBean(Class<T> requiredType) throws BeansException;
}