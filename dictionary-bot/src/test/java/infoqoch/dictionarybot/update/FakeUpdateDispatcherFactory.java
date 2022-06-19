package infoqoch.dictionarybot.update;

import infoqoch.dictionarybot.update.resolver.bean.MapBeanContext;
import infoqoch.dictionarybot.update.testcontroller.TestHandler;
import org.reflections.util.ClasspathHelper;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FakeUpdateDispatcherFactory {
    public static UpdateDispatcher instance(){
        Map<Class<?>, Object> context = new HashMap<>();
        final Object bean = new TestHandler();
        context.put(bean.getClass(), bean);

        MapBeanContext beanExtract = new MapBeanContext();
        beanExtract.setContext(context);

        final Collection<URL> urls = ClasspathHelper.forPackage(UpdateDispatcher.class.getPackage().getName());
        return new UpdateDispatcher(beanExtract, urls);
    }

}
