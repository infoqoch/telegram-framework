package infoqoch.dictionarybot.update.dispatcher.wrong1;

import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.resolver.bean.FakeMapBeanContext;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WrongUpdateDispatcherFactory {
    public static UpdateDispatcher defaultInstance(){
        return new UpdateDispatcher(fakeBeanContext(), testUrls());
    }

    private static ArrayList<URL> testUrls() {
        final URL resource = WrongReturnController.class.getResource(".");
        System.out.println("resource = " + resource);

        final ArrayList<URL> urls = new ArrayList<>();
        urls.add(resource);

        return urls;
    }


    private static FakeMapBeanContext fakeBeanContext() {
        Map<Class<?>, Object> context = new HashMap<>();

        final Object bean = new WrongReturnController();
        context.put(bean.getClass(), bean);

        FakeMapBeanContext beanExtract = new FakeMapBeanContext();
        beanExtract.setContext(context);
        return beanExtract;
    }
}
