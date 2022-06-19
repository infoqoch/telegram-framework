package infoqoch.dictionarybot.update;

import infoqoch.dictionarybot.DictionaryBotApplicationTests;
import infoqoch.dictionarybot.update.resolver.bean.FakeMapBeanContext;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FakeUpdateDispatcherFactory {
    public static UpdateDispatcher instance(){
        return new UpdateDispatcher(fakeBeanContext(), testUrls());
    }

    private static ArrayList<URL> testUrls() {
        final URL resource = DictionaryBotApplicationTests.class.getResource(".");
        System.out.println("resource = " + resource);

        final ArrayList<URL> urls = new ArrayList<>();
        urls.add(resource);
        return urls;
    }

    // !! 테스트에 한정하는 @UpdateRequestMethodMapper 의 객체(인스턴스)를 삽입해야한다.
    private static FakeMapBeanContext fakeBeanContext() {
        Map<Class<?>, Object> context = new HashMap<>();
        final Object bean = new FakeController();
        context.put(bean.getClass(), bean);

        FakeMapBeanContext beanExtract = new FakeMapBeanContext();
        beanExtract.setContext(context);
        return beanExtract;
    }
}
