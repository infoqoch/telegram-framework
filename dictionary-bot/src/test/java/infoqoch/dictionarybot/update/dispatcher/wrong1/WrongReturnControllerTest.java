package infoqoch.dictionarybot.update.dispatcher.wrong1;

import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.controller.resolver.UpdateRequestMethodResolverFactory;
import infoqoch.dictionarybot.update.controller.resolver.returns.*;
import infoqoch.dictionarybot.update.request.UpdateRequestMessage;
import infoqoch.dictionarybot.update.request.body.UpdateChat;
import infoqoch.dictionarybot.update.resolver.bean.FakeMapBeanContext;
import infoqoch.dictionarybot.update.controller.resolver.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WrongReturnControllerTest {
    // 지원하지 않는 데이터 타입에 대한 한정을 스프링 컨텍스트 로딩시점에서 제한할 필요가 있다. 현재는 런타임 시점에서 예외가 발생함.
    // 지금 지원은 어려움. 왜냐하면 param과 return의 resolver가 switch 형태를 가지고 있기 때문. 이를 수정한 후에 사용 가능.
    @Disabled("지원 필요")
    @Test
    @DisplayName("스프링 컨테이너 로딩 시점에서 지원하지 않는 return 값에 대해서 예외를 던진다.")
    void not_support_return_date_type(){
        assertThatThrownBy(()->{
            WrongUpdateDispatcherFactory.defaultInstance();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    // LocalDateTime은 지원하지 않는다.
    @UpdateRequestMethodMapper(EXCEL_PUSH)
    public LocalDateTime excelpush() {
        return LocalDateTime.now();
    }

    @UpdateRequestMethodMapper(LOOKUP_SENTENCE)
    public String lookupBySentence(UpdateRequestMessage request) {
        StringBuilder sb = new StringBuilder();
        return "LOOKUP_SENTENCE : " + request.getValue();
    }

    @UpdateRequestMethodMapper(LOOKUP_DEFINITION)
    public UpdateResponse lookupByDefinition(UpdateRequestMessage request) {
        return new UpdateResponse(SendType.MESSAGE, null);
    }

    @UpdateRequestMethodMapper(LOOKUP_WORD)
    public UpdateResponse lookupByWord(
            UpdateRequestMessage updateRequestMessage,
            UpdateChat chat
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append(updateRequestMessage.command()).append(" : ");
        sb.append(updateRequestMessage.value()).append(" : ");
        sb.append(chat.getMessageId());

        return new UpdateResponse(SendType.MESSAGE, new MarkdownStringBuilder(sb.toString()));
    }

    @UpdateRequestMethodMapper(HELP)
    public UpdateResponse help(UpdateRequestMessage request) {
        return new UpdateResponse(SendType.MESSAGE, new MarkdownStringBuilder("help! " + request.getValue()));
    }

    @UpdateRequestMethodMapper(UNKNOWN)
    public String unknown() {
        return "unknown??";
    }

    private static class WrongUpdateDispatcherFactory {
        public static UpdateDispatcher defaultInstance(){
            return new UpdateDispatcher(UpdateRequestMethodResolverFactory.collectUpdateRequestMappedMethods(fakeBeanContext(), testUrls(), null, returnResolvers()));
        }

        private static List<UpdateRequestReturn> returnResolvers(){
            List<UpdateRequestReturn> returnResolvers = new ArrayList<>();
            returnResolvers.add(new DictionaryUpdateRequestReturn());
            returnResolvers.add(new MSBUpdateRequestReturn());
            returnResolvers.add(new StringUpdateRequestReturn());
            returnResolvers.add(new DictionariesUpdateRequestReturn());
            return returnResolvers;
        }

        private static ArrayList<URL> testUrls() {
            final URL resource = WrongReturnControllerTest.class.getResource(".");
            System.out.println("resource = " + resource);

            final ArrayList<URL> urls = new ArrayList<>();
            urls.add(resource);

            return urls;
        }

        private static FakeMapBeanContext fakeBeanContext() {
            Map<Class<?>, Object> context = new HashMap<>();

            final Object bean = new WrongReturnControllerTest();
            context.put(bean.getClass(), bean);

            FakeMapBeanContext beanExtract = new FakeMapBeanContext();
            beanExtract.setContext(context);
            return beanExtract;
        }
    }
}
