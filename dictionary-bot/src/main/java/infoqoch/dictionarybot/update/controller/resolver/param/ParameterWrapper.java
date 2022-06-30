package infoqoch.dictionarybot.update.controller.resolver.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class ParameterWrapper {
    private final Parameter parameter;
    private final Annotation[] annotations;

    public ParameterWrapper(Parameter parameter) {
        this.parameter = parameter;
        this.annotations = parameter.getAnnotations();
    }

    public Class<?> type() {
        return parameter.getType();
    }

    public boolean hasAnnotation(Class<?> type){
        for (Annotation annotation : annotations) {
            if(annotation.annotationType() == type)
                return true;
        }
        return false;
    }

    public <A> A getAnnotationType(Class<A> type) {
        for (Annotation annotation : annotations) {
            if(annotation.annotationType() == type)
                return (A) annotation;
        }
        throw new IllegalStateException("!!!!!");
    }
}
