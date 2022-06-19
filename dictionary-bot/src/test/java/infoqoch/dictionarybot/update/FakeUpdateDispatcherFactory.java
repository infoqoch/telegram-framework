package infoqoch.dictionarybot.update;

import infoqoch.dictionarybot.DictionaryBotApplicationTests;
import infoqoch.dictionarybot.update.resolver.bean.MapBeanContext;
import infoqoch.dictionarybot.update.testcontroller.TestHandler;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FakeUpdateDispatcherFactory {
    public static UpdateDispatcher instance(){
        Map<Class<?>, Object> context = new HashMap<>();
        final Object bean = new TestHandler();
        context.put(bean.getClass(), bean);

        MapBeanContext beanExtract = new MapBeanContext();
        beanExtract.setContext(context);

        final URL resource = DictionaryBotApplicationTests.class.getResource(".");
        System.out.println("resource = " + resource);

        final ArrayList<URL> urls = new ArrayList<>();
        urls.add(resource);

        return new UpdateDispatcher(beanExtract, urls);
    }
}
