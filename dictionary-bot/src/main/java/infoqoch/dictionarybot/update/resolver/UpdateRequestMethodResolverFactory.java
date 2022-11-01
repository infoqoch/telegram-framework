package infoqoch.dictionarybot.update.resolver;

import infoqoch.dictionarybot.update.resolver.bean.BeanContext;
import infoqoch.dictionarybot.update.resolver.param.UpdateRequestParam;
import infoqoch.dictionarybot.update.resolver.returns.UpdateRequestReturn;
import infoqoch.dictionarybot.update.request.UpdateRequestCommand;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UpdateRequestMethodResolverFactory {
    public static  Map<UpdateRequestCommand, UpdateRequestMethodResolver> collectUpdateRequestMappedMethods(BeanContext context, Collection<URL> urls, List<UpdateRequestParam> paramResolvers, List<UpdateRequestReturn> returnResolvers) {
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
        final Set<UpdateRequestCommand> commands = concretedCommand.keySet();
        for (UpdateRequestCommand command : commands) {
            System.out.println(command);
            final UpdateRequestMethodResolver resolver = concretedCommand.get(command);
            System.out.println(resolver.toString());
        }

    }

    private static Set<Method> getMethodsAnnotated(Collection<URL> urls) {
        return new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(Scanners.MethodsAnnotated)).getMethodsAnnotatedWith(UpdateRequestMethodMapper.class);
    }

    private static UpdateRequestMethodMapper extractUpdateRequestMapper(Method method) {
        return (UpdateRequestMethodMapper) Arrays.stream(method.getDeclaredAnnotations()).filter(a -> a.annotationType() == UpdateRequestMethodMapper.class).findAny().get();
    }

}
