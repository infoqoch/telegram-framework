package infoqoch.dictionarybot.mock.update;

import infoqoch.dictionarybot.controller.resolver.DictionariesUpdateRequestReturn;
import infoqoch.dictionarybot.controller.resolver.DictionaryUpdateRequestReturn;
import infoqoch.dictionarybot.controller.resolver.UpdateChatUserRequestParam;
import infoqoch.telegram.framework.update.resolver.param.*;
import infoqoch.telegram.framework.update.resolver.returns.MSBUpdateRequestReturn;
import infoqoch.telegram.framework.update.resolver.returns.StringUpdateRequestReturn;
import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturn;
import infoqoch.telegram.framework.update.resolver.returns.UpdateResponseUpdateRequestReturn;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
// 테스트에 한정하는 @UpdateRequestMethodMapper 의 대역 객체(인스턴스)를 삽입해야한다. 이때 사용하는 대역은 FakeController이다.
public class FakeUpdateDispatcherFactory {
//    public static UpdateDispatcher defaultInstance(){
//        return new UpdateDispatcher(UpdateRequestMethodResolverFactory.collectUpdateRequestMappedMethods(fakeBeanContext(), testUrls(), paramResolvers(), returnResolvers()));
//    }

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
        paramResolvers.add(new ThrowRuntimeExceptionRequestParam());
        paramResolvers.add(new UpdateRequestUpdateRequestParam());
        paramResolvers.add(new UpdateRequestMessageUpdateRequestParam());
        paramResolvers.add(new UpdateChatUpdateRequestParam());
        paramResolvers.add(new UpdateDocumentUpdateRequestParam());
        paramResolvers.add(new UpdateChatUserRequestParam(null));
        paramResolvers.add(new TelegramPropertiesRequestParam(null));
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
