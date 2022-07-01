package infoqoch.dictionarybot.update.controller.resolver;

import infoqoch.dictionarybot.system.exception.TelegramServerException;
import infoqoch.dictionarybot.update.controller.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.controller.resolver.param.ParameterWrapper;
import infoqoch.dictionarybot.update.controller.resolver.param.UpdateRequestParam;
import infoqoch.dictionarybot.update.controller.resolver.returns.UpdateRequestReturn;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.response.UpdateResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

// TODO.
// 런타임이 아닌 스프링 컨텍스트 로딩 시점에서 지원하는 데이터타입이 아닌 타입을 시그니처로 했을 때 예외가 터지도록 변경 필요.
public class UpdateRequestMethodResolver {
    private final Object bean;
    private final Method method;
    private final UpdateRequestMethodMapper mapper;
    private final ParameterWrapper[] parameters;
    private final UpdateRequestReturn returnResolver;

    public UpdateRequestMethodResolver(Object bean, Method method, UpdateRequestMethodMapper mapper, List<UpdateRequestParam> paramResolvers, List<UpdateRequestReturn> returnResolvers) {
        this.bean = bean;
        this.method = method;
        this.mapper = mapper;
        this.parameters = extractParameterWrapper(paramResolvers);
        this.returnResolver = findReturnResolver(returnResolvers);
    }
    private UpdateRequestReturn findReturnResolver(List<UpdateRequestReturn> returnResolvers) {

        Optional<UpdateRequestReturn> resolver = returnResolvers.stream().filter(r -> r.support(method)).findAny();

        if(resolver.isEmpty())  throw new IllegalArgumentException("can not resolve the return type (1). return type : " + method.getReturnType());

        return resolver.get();
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
            final ParameterWrapper parameter = parameters[i];
            args[i] = parameter.resolve(update);
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

    private ParameterWrapper[] extractParameterWrapper(List<UpdateRequestParam> paramResolvers) {
        final Parameter[] parameters = method.getParameters();
        final ParameterWrapper[] parameterWrappers = new ParameterWrapper[parameters.length];
        for (int i=0; i<parameters.length; i++) {
            final UpdateRequestParam paramResolver = findParamResolver(parameters[i], paramResolvers);
            parameterWrappers[i] = new ParameterWrapper(parameters[i], paramResolver);
        }
        return parameterWrappers;
    }

    private UpdateRequestParam findParamResolver(Parameter parameter, List<UpdateRequestParam> paramResolvers) {
        Optional<UpdateRequestParam> resolver = paramResolvers.stream().filter(r -> r.support(parameter)).findAny();

        if(resolver.isEmpty())  throw new IllegalArgumentException("can not resolve the parameter type. parameter type : " + parameter.getType());

        return resolver.get();
    }
}
