package infoqoch.telegram.framework.update;

import infoqoch.telegram.framework.update.resolver.bean.BeanContext;
import infoqoch.telegram.framework.update.resolver.param.UpdateRequestParam;
import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturn;
import infoqoch.telegram.framework.update.request.UpdateRequestCommand;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
class UpdateRequestMethodResolverFactory {
    static  Map<UpdateRequestCommand, UpdateRequestMethodResolver> collectUpdateRequestMappedMethods(BeanContext context, Collection<URL> urls, List<UpdateRequestParam> paramResolvers, List<UpdateRequestReturn> returnResolvers) {
        Map<UpdateRequestCommand, UpdateRequestMethodResolver> concretedCommand = new ConcurrentHashMap<>();

        for (Method method : getMethodsAnnotated(urls)) {
            final UpdateRequestMethodMapper mapper = extractUpdateRequestMapper(method);
            final UpdateRequestMethodResolver resolver = new UpdateRequestMethodResolver(context.getBean(method.getDeclaringClass()), method, mapper, paramResolvers, returnResolvers);

            for (String commandStr : mapper.value()) {
                final UpdateRequestCommand command = UpdateRequestCommand.of(commandStr);

                if(concretedCommand.containsKey(command))
                    throw new IllegalStateException("duplicate declared command detected  : " + commandStr.toString());

                concretedCommand.put(command, resolver);
            }
        }

        print(concretedCommand);

        if(concretedCommand.get(UpdateRequestCommand.of("*"))==null)
            throw new IllegalStateException("* should be implements!");

        return concretedCommand;
    }

    private static void print(Map<UpdateRequestCommand, UpdateRequestMethodResolver> concretedCommand) {
        log.info("== print! == ");
        final Set<UpdateRequestCommand> commands = concretedCommand.keySet();
        log.info("candidates : {}", commands);
        for (UpdateRequestCommand command : commands) {
            final UpdateRequestMethodResolver resolver = concretedCommand.get(command);
            log.info("command : {}, resolver : {}", command, resolver.toString());
        }

    }

    private static Set<Method> getMethodsAnnotated(Collection<URL> urls) {
        return new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(Scanners.MethodsAnnotated)).getMethodsAnnotatedWith(UpdateRequestMethodMapper.class);
    }

    private static UpdateRequestMethodMapper extractUpdateRequestMapper(Method method) {
        return (UpdateRequestMethodMapper) Arrays.stream(method.getDeclaredAnnotations()).filter(a -> a.annotationType() == UpdateRequestMethodMapper.class).findAny().get();
    }

}
