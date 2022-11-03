package infoqoch.telegram.framework.update.resolver;

import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.resolver.param.UpdateRequestParam;
import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturn;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

public class UpdateRequestMethodResolver {
    private final Object bean;
    private final Method method;
    private final UpdateRequestMethodMapper mapper;
    private final UpdateRequestParam[] parameters;
    private final UpdateRequestReturn returnResolver;

    @Override
    public String toString() {
        return "UpdateRequestMethodResolver{"+method.getName()+"}";
    }

    public UpdateRequestMethodResolver(Object bean, Method method, UpdateRequestMethodMapper mapper, List<UpdateRequestParam> paramResolvers, List<UpdateRequestReturn> returnResolvers) {
        this.bean = bean;
        this.method = method;
        this.mapper = mapper;
        this.parameters = wrappingParameter(paramResolvers);
        this.returnResolver = findReturnResolver(returnResolvers);
    }

    public UpdateResponse process(UpdateRequest update) {
        Object[] args = resolveParameters(update);
        return resolveReturn(args);
    }

    private Object[] resolveParameters(UpdateRequest update) {
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            args[i] = parameters[i].resolve(update);
        }
        return args;
    }

    @SneakyThrows
    private UpdateResponse resolveReturn(Object[] args){
        try {
            return returnResolver.resolve(method.invoke(bean, args));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private UpdateRequestReturn findReturnResolver(List<UpdateRequestReturn> returnResolvers) {

        Optional<UpdateRequestReturn> resolver = returnResolvers.stream().filter(r -> r.support(method)).findAny();

        if(resolver.isEmpty())  throw new IllegalArgumentException("can not resolve the return type (1). return type : " + method.getReturnType());

        return resolver.get();
    }

    private UpdateRequestParam[] wrappingParameter(List<UpdateRequestParam> paramResolvers) {
        final Parameter[] parameters = method.getParameters();

        final UpdateRequestParam[] updateRequestParams = new UpdateRequestParam[parameters.length];
        for (int i=0; i<parameters.length; i++) {
            updateRequestParams[i] = findSupportParameterResolver(parameters[i], paramResolvers);
        }

        return updateRequestParams;
    }

    private UpdateRequestParam findSupportParameterResolver(Parameter parameter, List<UpdateRequestParam> paramResolvers) {
        Optional<UpdateRequestParam> resolver = paramResolvers.stream().filter(r -> r.support(parameter)).findAny();

        if(resolver.isEmpty())  throw new IllegalArgumentException("can not resolve the parameter type. parameter type : " + parameter.getType());

        return resolver.get();
    }
}
