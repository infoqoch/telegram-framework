package infoqoch.dictionarybot.update;

import infoqoch.dictionarybot.update.request.UpdateWrapper;
import infoqoch.dictionarybot.update.resolver.UpdateRequestMethodResolver;
import infoqoch.dictionarybot.update.resolver.bean.BeanContext;
import infoqoch.dictionarybot.update.resolver.mapper.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.*;

public class UpdateDispatcher {
    private final List<UpdateRequestMethodResolver> methodResolvers = new ArrayList<>();

    public UpdateDispatcher(String packagePath, BeanContext context) {
        collectUpdateRequestMappedMethods(packagePath, context);
    }

    public UpdateResponse process(UpdateWrapper update) {
        final Optional<UpdateRequestMethodResolver> any = methodResolvers.stream().filter(r -> r.support(update)).findAny();
        return any.get().process(update);
    }

    private void collectUpdateRequestMappedMethods(String packagePath, BeanContext context) {
        Set<UpdateRequestMethodMapper> updateRequestMappers = new HashSet<>();

        for (Method method : getMethodsAnnotated(packagePath)) {
            final UpdateRequestMethodMapper mapper = extractUpdateRequestMapper(method);

            checkDuplicatedMapper(updateRequestMappers, mapper);

            methodResolvers.add(new UpdateRequestMethodResolver(context.getBean(method.getDeclaringClass()), method, mapper));
        }
    }

    private UpdateRequestMethodMapper extractUpdateRequestMapper(Method method) {
        return (UpdateRequestMethodMapper) Arrays.stream(method.getDeclaredAnnotations()).filter(a -> a.annotationType() == UpdateRequestMethodMapper.class).findAny().get();
    }

    private Set<Method> getMethodsAnnotated(String packagePath) {
        return new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(packagePath)).setScanners(Scanners.MethodsAnnotated)).getMethodsAnnotatedWith(UpdateRequestMethodMapper.class);
    }

    private void checkDuplicatedMapper(Set<UpdateRequestMethodMapper> checkDuplicatedMapper, UpdateRequestMethodMapper mapper) {
        if (checkDuplicatedMapper.contains(mapper))
            throw new IllegalStateException("중복 UpdateRequestMapper 는 불가능 합니다 : " + mapper.toString());
        checkDuplicatedMapper.add(mapper);
    }

}