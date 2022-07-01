package infoqoch.dictionarybot.update.controller.resolver;

import infoqoch.dictionarybot.system.exception.TelegramServerException;
import infoqoch.dictionarybot.update.controller.resolver.param.UpdateRequestParam;
import infoqoch.dictionarybot.update.controller.resolver.returns.UpdateRequestReturn;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.response.UpdateResponse;

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

    public UpdateRequestMethodResolver(Object bean, Method method, UpdateRequestMethodMapper mapper, List<UpdateRequestParam> paramResolvers, List<UpdateRequestReturn> returnResolvers) {
        this.bean = bean;
        this.method = method;
        this.mapper = mapper;
        this.parameters = wrappingParameter(paramResolvers);
        this.returnResolver = findReturnResolver(returnResolvers);
    }

    public boolean support(UpdateRequest update) {
        return mapper.value() == update.command();
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

    private UpdateResponse resolveReturn(Object[] args){
        try {
            return returnResolver.resolve(method.invoke(bean, args));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new TelegramServerException("can not resolve the return data (2)", e);
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
