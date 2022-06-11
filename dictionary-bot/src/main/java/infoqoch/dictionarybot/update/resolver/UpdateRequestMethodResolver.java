package infoqoch.dictionarybot.update.resolver;

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

public class UpdateRequestMethodResolver {
    private final Object bean;
    private final Method method;
    private final UpdateRequestMethodMapper mapper;
    private final ParameterWrapper[] parameters;

    public UpdateRequestMethodResolver(Object bean, Method method, UpdateRequestMethodMapper mapper) {
        this.bean = bean;
        this.method = method;
        this.mapper = mapper;
        this.parameters = extractParameterWrapper(this.method);
    }

    public boolean support(UpdateWrapper update) {
        return mapper.value() == update.command();
    }

    public UpdateResponse process(UpdateWrapper update) {
        Object[] args = new Object[parameters.length];

        for(int i=0; i< parameters.length; i++){
            final ParameterWrapper parameter = parameters[i];
            final Class<?> type = parameter.type();

            if(type == UpdateWrapper.class){
                args[i] = update;
                continue;
            }

            if(type == UpdateRequest.class){
                args[i] = update.updateRequest();
                continue;
            }

            if(parameter.hasAnnotation(UpdateRequestBodyParameterMapper.class)){
                Optional<Object> body = update.getBodyByType(type);
                if(body.isEmpty())
                    throw new IllegalArgumentException("잘못된 요구!");
                args[i] = body.get();
                continue;
            }
        }

        try {
            final Object result = method.invoke(bean, args);

            if(result instanceof String)
                return new UpdateResponse(SendType.MESSAGE, result);
            return (UpdateResponse) result;

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("!!!!!@#$#@");
    }

    private ParameterWrapper[] extractParameterWrapper(Method method) {
        final Parameter[] parameters = method.getParameters();
        final ParameterWrapper[] parameterWrappers = new ParameterWrapper[parameters.length];
        for (int i=0; i<parameters.length; i++) {
            parameterWrappers[i] = new ParameterWrapper(parameters[i]);
        }
        return parameterWrappers;
    }
}
