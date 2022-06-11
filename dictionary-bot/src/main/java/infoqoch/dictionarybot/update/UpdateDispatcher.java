package infoqoch.dictionarybot.update;

import infoqoch.dictionarybot.update.request.body.UpdateWrapper;
import infoqoch.dictionarybot.update.resolver.UpdateRequestMethodResolver;
import infoqoch.dictionarybot.update.resolver.mapper.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.resolver.bean.BeanContext;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.*;

public class UpdateDispatcher {
    private final List<UpdateRequestMethodResolver> methodResolvers;

    public UpdateDispatcher(String packagePath, BeanContext context) {
        methodResolvers = collectUpdateRequestMappedMethods(packagePath, context);
    }

    private List<UpdateRequestMethodResolver> collectUpdateRequestMappedMethods(String packagePath, BeanContext context) {
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(packagePath)).setScanners(Scanners.MethodsAnnotated));
        final Set<Method> methods = reflections.getMethodsAnnotatedWith(UpdateRequestMethodMapper.class);

        List<UpdateRequestMethodResolver> resolvers = new ArrayList<>();
        Set<UpdateRequestMethodMapper> updateRequestMappers = new HashSet<>();

        for (Method method : methods) {
            final Object bean = context.getBean(method.getDeclaringClass());
            final UpdateRequestMethodMapper mapper = (UpdateRequestMethodMapper) Arrays.stream(method.getDeclaredAnnotations()).filter(a -> a.annotationType() == UpdateRequestMethodMapper.class).findAny().get();

            checkDuplicatedMapper(updateRequestMappers, mapper);

            resolvers.add(new UpdateRequestMethodResolver(bean, method, mapper));
        }
        return resolvers;
    }

    private void checkDuplicatedMapper(Set<UpdateRequestMethodMapper> checkDuplicatedMapper, UpdateRequestMethodMapper mapper) {
        if (checkDuplicatedMapper.contains(mapper))
            throw new IllegalStateException("중복 UpdateRequestMapper 는 불가능 합니다 : " + mapper.toString());
        checkDuplicatedMapper.add(mapper);
    }

    public UpdateResponse process(UpdateWrapper update) {
        final Optional<UpdateRequestMethodResolver> any = methodResolvers.stream().filter(r -> r.support(update)).findAny();
        final UpdateRequestMethodResolver updateRequestMethodResolver = any.get();
        return updateRequestMethodResolver.process(update);
    }
}