package infoqoch.telegram.framework.update;

import infoqoch.telegram.framework.update.request.UpdateRequestCommand;
import infoqoch.telegram.framework.update.resolver.param.UpdateRequestParamRegister;
import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturnRegister;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
class UpdateRequestMapperFactory {
    static  Map<UpdateRequestCommand, UpdateRequestResolver> collectUpdateRequestMappedMethods(BeanFactory beanFactory, Collection<URL> urls, UpdateRequestParamRegister paramResolvers, UpdateRequestReturnRegister returnResolvers) {
        Map<UpdateRequestCommand, UpdateRequestResolver> concretedCommand = new ConcurrentHashMap<>();

        for (Method method : getMethodsAnnotated(urls)) {
            final UpdateRequestMapper mapper = extractUpdateRequestMapper(method);
            final UpdateRequestResolver resolver = new UpdateRequestResolver(beanFactory.getBean(method.getDeclaringClass()), method, mapper, paramResolvers, returnResolvers);

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

    private static void print(Map<UpdateRequestCommand, UpdateRequestResolver> concretedCommand) {
        log.info("== print! == ");
        final Set<UpdateRequestCommand> commands = concretedCommand.keySet();
        log.info("candidates : {}", commands);
        for (UpdateRequestCommand command : commands) {
            final UpdateRequestResolver resolver = concretedCommand.get(command);
            log.info("command : {}, resolver : {}", command, resolver.toString());
        }

    }

    private static Set<Method> getMethodsAnnotated(Collection<URL> urls) {
        return new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(Scanners.MethodsAnnotated)).getMethodsAnnotatedWith(UpdateRequestMapper.class);
    }

    private static UpdateRequestMapper extractUpdateRequestMapper(Method method) {
        return (UpdateRequestMapper) Arrays.stream(method.getDeclaredAnnotations()).filter(a -> a.annotationType() == UpdateRequestMapper.class).findAny().get();
    }

}
