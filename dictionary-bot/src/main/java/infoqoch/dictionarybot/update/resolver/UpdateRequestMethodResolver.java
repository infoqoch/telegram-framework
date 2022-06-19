package infoqoch.dictionarybot.update.resolver;

import infoqoch.dictionarybot.system.exception.TelegramServerException;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.request.UpdateWrapper;
import infoqoch.dictionarybot.update.resolver.mapper.UpdateRequestBodyParameterMapper;
import infoqoch.dictionarybot.update.resolver.mapper.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

// TODO.
// 런타임이 아닌 스프링 컨텍스트 로딩 시점에서 지원하는 데이터타입이 아닌 타입을 시그니처로 했을 때 예외가 터지도록 변경 필요.
public class UpdateRequestMethodResolver {
    private final Object bean;
    private final Method method;
    private final UpdateRequestMethodMapper mapper;
    private final ParameterWrapper[] parameters;

    public UpdateRequestMethodResolver(Object bean, Method method, UpdateRequestMethodMapper mapper) {
        this.bean = bean;
        this.method = method;
        this.mapper = mapper;
        this.parameters = extractParameterWrapper();
    }

    public boolean support(UpdateWrapper update) {
        return mapper.value() == update.command();
    }

    public UpdateResponse process(UpdateWrapper update) {
        Object[] args = resolveParameters(update);
        return resolveReturn(args);
    }

    private Object[] resolveParameters(UpdateWrapper update) {
        Object[] args = new Object[parameters.length];

        for(int i=0; i< parameters.length; i++){
            final ParameterWrapper parameter = parameters[i];

            if(parameter.type() == UpdateWrapper.class){
                args[i] = update;
                continue;
            }

            if(parameter.type() == UpdateRequest.class){
                args[i] = update.updateRequest();
                continue;
            }

            if(parameter.hasAnnotation(UpdateRequestBodyParameterMapper.class)){
                Optional<Object> body = update.getBodyByType(parameter.type());
                if(body.isEmpty())
                    throw new TelegramServerException("not support parameter type. UpdateChat, UpdateDocument supported only");
                args[i] = body.get();
                continue;
            }
        }
        return args;
    }

    private UpdateResponse resolveReturn(Object[] args){
        try {
            final Object result = method.invoke(bean, args);
            if(result instanceof String)
                return new UpdateResponse(SendType.MESSAGE, result);
            if(result instanceof UpdateResponse)
                return (UpdateResponse) result;
            throw new TelegramServerException("can not resolve the return data (1) : " + result.getClass());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new TelegramServerException("can not resolve the return data (2)", e);
        }
    }

    private ParameterWrapper[] extractParameterWrapper() {
        final Parameter[] parameters = method.getParameters();
        final ParameterWrapper[] parameterWrappers = new ParameterWrapper[parameters.length];
        for (int i=0; i<parameters.length; i++) {
            parameterWrappers[i] = new ParameterWrapper(parameters[i]);
        }
        return parameterWrappers;
    }
}
