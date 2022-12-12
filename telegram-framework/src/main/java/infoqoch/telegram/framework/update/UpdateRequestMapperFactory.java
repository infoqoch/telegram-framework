package infoqoch.telegram.framework.update;

import infoqoch.telegram.framework.update.request.UpdateRequestCommand;
import infoqoch.telegram.framework.update.resolver.param.UpdateRequestParamRegister;
import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturnRegister;
import infoqoch.telegram.framework.update.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
class UpdateRequestMapperFactory {
    static  Map<UpdateRequestCommand, UpdateRequestResolver> collectUpdateRequestMappedMethods(BeanFactory beanFactory, Object basePackage, UpdateRequestParamRegister paramResolvers, UpdateRequestReturnRegister returnResolvers) {
        Map<UpdateRequestCommand, UpdateRequestResolver> concretedCommand = new ConcurrentHashMap<>();

        for (Method method : getMethodsAnnotated(basePackage)) {
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
            throw new IllegalStateException("* should be implemented!");

        return concretedCommand;
    }

    private static void print(Map<UpdateRequestCommand, UpdateRequestResolver> concretedCommand) {
        final Set<UpdateRequestCommand> commands = concretedCommand.keySet();
        log.info("candidates : {}", commands);
        for (UpdateRequestCommand command : commands) {
            final UpdateRequestResolver resolver = concretedCommand.get(command);
            log.info("command : {}, resolver : {}", command, resolver.toString());
        }

    }

    private static Set<Method> getMethodsAnnotated(Object base) {
        String basePackage = base.getClass().getPackage().getName();
        ConfigurationBuilder configuration = new ConfigurationBuilder()
                .forPackages(basePackage)
                .setScanners(Scanners.MethodsAnnotated);
        ReflectionUtil.ifJarThenCalibrating(configuration, basePackage);
        return new Reflections(configuration)
                .getMethodsAnnotatedWith(UpdateRequestMapper.class);
    }

    private static UpdateRequestMapper extractUpdateRequestMapper(Method method) {
        return (UpdateRequestMapper) Arrays.stream(method.getDeclaredAnnotations()).filter(a -> a.annotationType() == UpdateRequestMapper.class).findAny().get();
    }

}
