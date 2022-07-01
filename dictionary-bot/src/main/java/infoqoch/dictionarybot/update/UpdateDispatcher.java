package infoqoch.dictionarybot.update;

import infoqoch.dictionarybot.update.controller.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.controller.resolver.param.UpdateRequestParam;
import infoqoch.dictionarybot.update.request.UpdateRequestCommand;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.controller.resolver.UpdateRequestMethodResolver;
import infoqoch.dictionarybot.update.controller.resolver.bean.BeanContext;
import infoqoch.dictionarybot.update.controller.resolver.returns.UpdateRequestReturn;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class UpdateDispatcher {
    private final List<UpdateRequestMethodResolver> methodResolvers = new ArrayList<>();

    // 실제 동작
    public UpdateResponse process(UpdateRequest update) {
        return methodResolvers.stream().filter(r -> r.support(update)).findAny().get().process(update);
    }

    // 이후 factory로 delegate 할 소스들
    public UpdateDispatcher(BeanContext context, Collection<URL> urls, List<UpdateRequestParam> paramResolvers, List<UpdateRequestReturn> returnResolvers) {
        collectUpdateRequestMappedMethods(context, urls, paramResolvers,  returnResolvers);
    }

    private void collectUpdateRequestMappedMethods(BeanContext context, Collection<URL> urls, List<UpdateRequestParam> paramResolvers, List<UpdateRequestReturn> returnResolvers) {
        Set<UpdateRequestMethodMapper> updateRequestMappers = new HashSet<>();

        for (Method method : getMethodsAnnotated(urls)) {
            final UpdateRequestMethodMapper mapper = extractUpdateRequestMapper(method);

            checkDuplicatedMapper(updateRequestMappers, mapper);

            methodResolvers.add(new UpdateRequestMethodResolver(context.getBean(method.getDeclaringClass()), method, mapper, paramResolvers, returnResolvers));
        }

        if(isNotConcretedEveryCommand(updateRequestMappers)){
            throw new IllegalArgumentException("every mapper should be concreted. declared with mapper annotation commands: " + printAllCommands());
        }
    }

    private Set<Method> getMethodsAnnotated(Collection<URL> urls) {
        return new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(Scanners.MethodsAnnotated)).getMethodsAnnotatedWith(UpdateRequestMethodMapper.class);
    }

    private UpdateRequestMethodMapper extractUpdateRequestMapper(Method method) {
        return (UpdateRequestMethodMapper) Arrays.stream(method.getDeclaredAnnotations()).filter(a -> a.annotationType() == UpdateRequestMethodMapper.class).findAny().get();
    }

    private void checkDuplicatedMapper(Set<UpdateRequestMethodMapper> checkDuplicatedMapper, UpdateRequestMethodMapper mapper) {
        if (checkDuplicatedMapper.contains(mapper))
            throw new IllegalStateException("duplicate declared command detected  : " + mapper.toString());
        checkDuplicatedMapper.add(mapper);
    }

    private boolean isNotConcretedEveryCommand(Set<UpdateRequestMethodMapper> updateRequestMappers) {
        return updateRequestMappers.size() != UpdateRequestCommand.values().length;
    }

    private String printAllCommands() {
        StringBuilder sb = new StringBuilder();
        for(UpdateRequestCommand command : UpdateRequestCommand.values() ){
            sb.append(command).append(" ");
        }
        return sb.toString();
    }
}