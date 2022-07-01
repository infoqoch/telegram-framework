package infoqoch.dictionarybot.update.controller.resolver.param;

import infoqoch.dictionarybot.update.request.UpdateRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class ParameterWrapper {
    private final Parameter parameter;
    private final Annotation[] annotations;
    private final UpdateRequestParam paramResolver;

    public ParameterWrapper(Parameter parameter, UpdateRequestParam paramResolver) {
        this.parameter = parameter;
        this.paramResolver = paramResolver;
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

    public Object resolve(UpdateRequest update) {
        return paramResolver.resolve(update);
    }
}
