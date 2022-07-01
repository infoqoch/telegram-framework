package infoqoch.dictionarybot.update.controller.resolver;

import infoqoch.dictionarybot.update.controller.resolver.bean.BeanContext;
import infoqoch.dictionarybot.update.controller.resolver.param.UpdateRequestParam;
import infoqoch.dictionarybot.update.controller.resolver.returns.UpdateRequestReturn;
import infoqoch.dictionarybot.update.request.UpdateRequestCommand;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class UpdateRequestMethodResolverFactory {
    public static  List<UpdateRequestMethodResolver> collectUpdateRequestMappedMethods(BeanContext context, Collection<URL> urls, List<UpdateRequestParam> paramResolvers, List<UpdateRequestReturn> returnResolvers) {
        List<UpdateRequestMethodResolver> methodResolvers = new ArrayList<>();

        Set<UpdateRequestMethodMapper> updateRequestMappers = new HashSet<>();

        for (Method method : getMethodsAnnotated(urls)) {
            final UpdateRequestMethodMapper mapper = extractUpdateRequestMapper(method);

            checkDuplicatedMapper(updateRequestMappers, mapper);

            methodResolvers.add(new UpdateRequestMethodResolver(context.getBean(method.getDeclaringClass()), method, mapper, paramResolvers, returnResolvers));
        }

        if(isNotConcretedEveryCommand(updateRequestMappers)){
            throw new IllegalArgumentException("every mapper should be concreted. declared with mapper annotation commands: " + printAllCommands());
        }

        return methodResolvers;
    }

    private static Set<Method> getMethodsAnnotated(Collection<URL> urls) {
        return new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(Scanners.MethodsAnnotated)).getMethodsAnnotatedWith(UpdateRequestMethodMapper.class);
    }

    private static UpdateRequestMethodMapper extractUpdateRequestMapper(Method method) {
        return (UpdateRequestMethodMapper) Arrays.stream(method.getDeclaredAnnotations()).filter(a -> a.annotationType() == UpdateRequestMethodMapper.class).findAny().get();
    }

    private static void checkDuplicatedMapper(Set<UpdateRequestMethodMapper> checkDuplicatedMapper, UpdateRequestMethodMapper mapper) {
        if (checkDuplicatedMapper.contains(mapper))
            throw new IllegalStateException("duplicate declared command detected  : " + mapper.toString());
        checkDuplicatedMapper.add(mapper);
    }

    private static boolean isNotConcretedEveryCommand(Set<UpdateRequestMethodMapper> updateRequestMappers) {
        return updateRequestMappers.size() != UpdateRequestCommand.values().length;
    }

    private static String printAllCommands() {
        StringBuilder sb = new StringBuilder();
        for(UpdateRequestCommand command : UpdateRequestCommand.values() ){
            sb.append(command).append(" ");
        }
        return sb.toString();
    }
}
