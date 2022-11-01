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

public class UpdateRequestMethodResolverFactory {
    public static  List<UpdateRequestMethodResolver> collectUpdateRequestMappedMethods(BeanContext context, Collection<URL> urls, List<UpdateRequestParam> paramResolvers, List<UpdateRequestReturn> returnResolvers) {
        List<UpdateRequestMethodResolver> methodResolvers = new ArrayList<>();

        Set<UpdateRequestCommand> concretedCommand = new HashSet<>();

        for (Method method : getMethodsAnnotated(urls)) {
            final UpdateRequestMethodMapper mapper = extractUpdateRequestMapper(method);

            checkDuplicatedCommand(concretedCommand, mapper.value());

            methodResolvers.add(new UpdateRequestMethodResolver(context.getBean(method.getDeclaringClass()), method, mapper, paramResolvers, returnResolvers));
        }

        isConcretedEveryCommand(concretedCommand);

        return methodResolvers;
    }

    private static Set<Method> getMethodsAnnotated(Collection<URL> urls) {
        return new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(Scanners.MethodsAnnotated)).getMethodsAnnotatedWith(UpdateRequestMethodMapper.class);
    }

    private static UpdateRequestMethodMapper extractUpdateRequestMapper(Method method) {
        return (UpdateRequestMethodMapper) Arrays.stream(method.getDeclaredAnnotations()).filter(a -> a.annotationType() == UpdateRequestMethodMapper.class).findAny().get();
    }

    private static void checkDuplicatedCommand(Set<UpdateRequestCommand> concretedCommand, UpdateRequestCommand[] inputCommand) {
        if (concretedCommand.contains(inputCommand))
            throw new IllegalStateException("duplicate declared command detected  : " + inputCommand.toString());

        for (UpdateRequestCommand input : inputCommand)
            concretedCommand.add(input);


    }

    private static void isConcretedEveryCommand(Set<UpdateRequestCommand> concretedCommand) {
        if(concretedCommand.size() != UpdateRequestCommand.values().length){
            throw new IllegalArgumentException("every mapper should be concreted. declared with mapper annotation commands: " + printAllCommands());
        }
    }

    private static String printAllCommands() {
        StringBuilder sb = new StringBuilder();
        for(UpdateRequestCommand command : UpdateRequestCommand.values() ){
            sb.append(command).append(" ");
        }
        return sb.toString();
    }
}
