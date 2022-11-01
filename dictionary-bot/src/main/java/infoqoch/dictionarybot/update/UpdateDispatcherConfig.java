package infoqoch.dictionarybot.update;

import infoqoch.dictionarybot.model.user.ChatUserRepository;
import infoqoch.dictionarybot.system.properties.TelegramProperties;
import infoqoch.dictionarybot.update.resolver.UpdateRequestMethodResolver;
import infoqoch.dictionarybot.update.resolver.UpdateRequestMethodResolverFactory;
import infoqoch.dictionarybot.update.resolver.bean.SpringBeanContext;
import infoqoch.dictionarybot.update.resolver.param.*;
import infoqoch.dictionarybot.update.resolver.returns.*;
import lombok.RequiredArgsConstructor;
import org.reflections.util.ClasspathHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class UpdateDispatcherConfig {
    private final ChatUserRepository chatUserRepository;
    private final TelegramProperties telegramProperties;

    @Bean
    public List<UpdateRequestReturn> returnResolvers(){
        List<UpdateRequestReturn> returnResolvers = new ArrayList<>();
        returnResolvers.add(new DictionaryUpdateRequestReturn());
        returnResolvers.add(new DictionariesUpdateRequestReturn());
        returnResolvers.add(new MSBUpdateRequestReturn());
        returnResolvers.add(new StringUpdateRequestReturn());
        returnResolvers.add(new DictionariesUpdateRequestReturn());
        returnResolvers.add(new UpdateResponseUpdateRequestReturn());
        return returnResolvers;
    }

    @Bean
    public List<UpdateRequestParam> paramResolvers(){
        List<UpdateRequestParam> paramResolvers = new ArrayList<>();
        paramResolvers.add(new UpdateRequestUpdateRequestParam());
        paramResolvers.add(new UpdateRequestMessageUpdateRequestParam());
        paramResolvers.add(new UpdateChatUpdateRequestParam());
        paramResolvers.add(new UpdateDocumentUpdateRequestParam());
        paramResolvers.add(new UpdateChatUserRequestParam(chatUserRepository));
        paramResolvers.add(new TelegramPropertiesRequestParam(telegramProperties));
        return paramResolvers;
    }

    @Bean
    public UpdateDispatcher updateDispatcher(ApplicationContext context){
        final SpringBeanContext springContext = new SpringBeanContext(context);
        final Collection<URL> urls = getUrlsExcludeTest();
        final List<UpdateRequestMethodResolver> methodResolvers = UpdateRequestMethodResolverFactory.collectUpdateRequestMappedMethods(springContext, urls, paramResolvers(), returnResolvers());
        return new UpdateDispatcher(methodResolvers);
    }

    // TODO
    // 통합 테스트(@SpringBootTest)가 동작할 때 클래스로더가 두 개 동작하며 그것의 절대 경로는 ../target/test-classes 와 ../target/classes 이다.
    // 실제 스프링 컨테이너를 로딩할 때는, 테스트를 사용하지 않으므로 이러한 코드가 필요 없다. 테스트를 할 때는 두 개의 클래스로더가 동작하기 때문에 불가피하게 아래와 같이 "/test-classes"에 있는 파일을 읽지 않도록 한다.
    // 운영이 테스트를 위한 코드에 종속되는 것은 좋지 않아서 최대한 해소하려 하였으나 어쩔 수 없이 아래와 같이 코드를 작성하였음.
    // 이에 대한 개선책이 반드시 필요함.
    private Set<URL> getUrlsExcludeTest() {

        final Set<URL> collect = ClasspathHelper.forPackage(UpdateDispatcher.class.getPackageName()).stream()
                .filter(url -> !url.toString().contains("/test-classes")) // maven
                .filter(url -> !url.toString().contains("/test/classes")) // gradle
                .filter(url -> !url.toString().contains("/java/test/")) // gradle test
                .collect(Collectors.toSet());
        for (URL url : collect) {
            System.out.println("url = " + url);
        }
        return collect;
    }
}
