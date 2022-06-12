package infoqoch.dictionarybot.update;

import infoqoch.dictionarybot.update.resolver.bean.MapBeanContext;
import infoqoch.dictionarybot.update.testcontroller.TestHandler;

import java.util.HashMap;
import java.util.Map;

public class FakeUpdateDispatcherFactory {
    public static UpdateDispatcher instance(){
        Map<Class<?>, Object> context = new HashMap<>();
        final Object bean = new TestHandler();
        context.put(bean.getClass(), bean);

        MapBeanContext beanExtract = new MapBeanContext();
        beanExtract.setContext(context);

        final String path = UpdateDispatcher.class.getPackage().getName() + ".testcontroller";
        return new UpdateDispatcher(path, beanExtract);
    }

}
