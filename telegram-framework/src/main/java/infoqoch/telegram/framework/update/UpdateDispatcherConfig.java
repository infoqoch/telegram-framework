package infoqoch.telegram.framework.update;

import infoqoch.telegram.framework.update.request.UpdateRequestCommand;
import infoqoch.telegram.framework.update.resolver.UpdateRequestMethodResolver;
import infoqoch.telegram.framework.update.resolver.UpdateRequestMethodResolverFactory;
import infoqoch.telegram.framework.update.resolver.bean.SpringBeanContext;
import infoqoch.telegram.framework.update.resolver.custom.CustomUpdateRequestParam;
import infoqoch.telegram.framework.update.resolver.custom.CustomUpdateRequestReturn;
import infoqoch.telegram.framework.update.resolver.param.*;
import infoqoch.telegram.framework.update.resolver.returns.MSBUpdateRequestReturn;
import infoqoch.telegram.framework.update.resolver.returns.StringUpdateRequestReturn;
import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturn;
import infoqoch.telegram.framework.update.resolver.returns.UpdateResponseUpdateRequestReturn;
import infoqoch.telegram.framework.update.util.TelegramProperties;
import infoqoch.telegrambot.bot.DefaultTelegramBotFactory;
import infoqoch.telegrambot.bot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.reflections.util.ClasspathHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@ComponentScan
@RequiredArgsConstructor
public class UpdateDispatcherConfig {
    private final CustomUpdateRequestParam customUpdateRequestParam;
    private final CustomUpdateRequestReturn customUpdateRequestReturn;

    @Bean
    public TelegramProperties telegramProperties(){
        return TelegramProperties.generate();
    }

    @Bean
    @Order(Integer.MAX_VALUE-101)
    public List<UpdateRequestReturn> returnResolvers(){
        List<UpdateRequestReturn> returnResolvers = new ArrayList<>();
        returnResolvers.add(new MSBUpdateRequestReturn());
        returnResolvers.add(new StringUpdateRequestReturn());
        returnResolvers.add(new UpdateResponseUpdateRequestReturn());
        customUpdateRequestReturn.addUpdateRequestReturn().stream().forEach(
                r -> returnResolvers.add(r)
        );

        return returnResolvers;
    }

    @Bean
    TelegramBot telegramBot(){
        return DefaultTelegramBotFactory.init(telegramProperties().token());
    }

    @Bean
    @Order(Integer.MAX_VALUE-100)
    public List<UpdateRequestParam> paramResolvers(){
        List<UpdateRequestParam> paramResolvers = new ArrayList<>();
        paramResolvers.add(new UpdateRequestUpdateRequestParam());
        paramResolvers.add(new UpdateRequestMessageUpdateRequestParam());
        paramResolvers.add(new UpdateChatUpdateRequestParam());
        paramResolvers.add(new UpdateDocumentUpdateRequestParam());

        paramResolvers.add(new TelegramPropertiesRequestParam(telegramProperties()));

        customUpdateRequestParam.addUpdateRequestParam().stream().forEach(
                r -> paramResolvers.add(r)
        );

        return paramResolvers;
    }

    @Bean
    public UpdateDispatcher updateDispatcher(ApplicationContext context){
        final SpringBeanContext springContext = new SpringBeanContext(context);
        final Collection<URL> urls = getUrlsExcludeTest(context);
        final Map<UpdateRequestCommand, UpdateRequestMethodResolver> methodResolvers
                = UpdateRequestMethodResolverFactory.collectUpdateRequestMappedMethods(springContext, urls, paramResolvers(), returnResolvers());
        return new UpdateDispatcher(methodResolvers);
    }

    // TODO
    // 통합 테스트(@SpringBootTest)가 동작할 때 클래스로더가 두 개 동작하며 그것의 절대 경로는 ../target/test-classes 와 ../target/classes 이다.
    // 실제 스프링 컨테이너를 로딩할 때는, 테스트를 사용하지 않으므로 이러한 코드가 필요 없다. 테스트를 할 때는 두 개의 클래스로더가 동작하기 때문에 불가피하게 아래와 같이 "/test-classes"에 있는 파일을 읽지 않도록 한다.
    // 운영이 테스트를 위한 코드에 종속되는 것은 좋지 않아서 최대한 해소하려 하였으나 어쩔 수 없이 아래와 같이 코드를 작성하였음.
    // 이에 대한 개선책이 반드시 필요함.

    // TODO
    // 아래 url 선택 방식은 수정 필요함.
    // 애당초 아래의 어너테이션이 없을 경우 프레임워크는 동작할 수 없음.
    private Set<URL> getUrlsExcludeTest(ApplicationContext context) {
        final Object listener = context.getBeansWithAnnotation(EnableTelegramFramework.class).values().stream().findAny().get();
        final Set<URL> collect = ClasspathHelper.forPackage(listener.getClass().getPackageName()).stream()
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
