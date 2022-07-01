package infoqoch.dictionarybot.mock.update;

import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.controller.resolver.param.*;
import infoqoch.dictionarybot.update.controller.resolver.returns.*;
import infoqoch.dictionarybot.update.resolver.bean.FakeMapBeanContext;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 테스트에 한정하는 @UpdateRequestMethodMapper 의 객체(인스턴스)를 삽입해야한다.
// mapper가 존재하는 위치를 탐색할 때 사용하는 url을 삽입한다.
// 기본 값은 기존의 FakeController를 가리키며 필요시 값을 삽입하여 사용한다.
public class FakeUpdateDispatcherFactory {
    // test를 fake로 사용하는 UpdateDispatcher
    // TODO
    public static UpdateDispatcher defaultInstance(){
        return new UpdateDispatcher(fakeBeanContext(), testUrls(), paramResolvers(), returnResolvers());
    }

    private static List<UpdateRequestReturn> returnResolvers(){
        List<UpdateRequestReturn> returnResolvers = new ArrayList<>();
        returnResolvers.add(new DictionaryUpdateRequestReturn());
        returnResolvers.add(new MSBUpdateRequestReturn());
        returnResolvers.add(new StringUpdateRequestReturn());
        returnResolvers.add(new DictionariesUpdateRequestReturn());
        returnResolvers.add(new UpdateResponseUpdateRequestReturn());
        return returnResolvers;
    }

    public static List<UpdateRequestParam> paramResolvers(){
        List<UpdateRequestParam> paramResolvers = new ArrayList<>();
        paramResolvers.add(new UpdateRequestUpdateRequestParam());
        paramResolvers.add(new UpdateRequestMessageUpdateRequestParam());
        paramResolvers.add(new UpdateChatUpdateRequestParam());
        paramResolvers.add(new UpdateDocumentUpdateRequestParam());
        return paramResolvers;
    }

    private static ArrayList<URL> testUrls() {
        final URL resource = FakeController.class.getResource(".");
        System.out.println("resource = " + resource);

        final ArrayList<URL> urls = new ArrayList<>();
        urls.add(resource);

        return urls;
    }


    private static FakeMapBeanContext fakeBeanContext() {
        Map<Class<?>, Object> context = new HashMap<>();

        final Object bean = new FakeController();
        context.put(bean.getClass(), bean);

        FakeMapBeanContext beanExtract = new FakeMapBeanContext();
        beanExtract.setContext(context);
        return beanExtract;
    }
}
