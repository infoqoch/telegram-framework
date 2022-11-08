package infoqoch.telegram.framework.update;

import infoqoch.telegram.framework.update.file.TelegramFileHandler;
import infoqoch.telegram.framework.update.request.UpdateRequestCommand;
import infoqoch.telegram.framework.update.resolver.bean.SpringBeanContext;
import infoqoch.telegram.framework.update.resolver.custom.CustomUpdateRequestParamRegister;
import infoqoch.telegram.framework.update.resolver.custom.CustomUpdateRequestReturnRegister;
import infoqoch.telegram.framework.update.resolver.param.*;
import infoqoch.telegram.framework.update.resolver.returns.*;
import infoqoch.telegram.framework.update.util.TelegramProperties;
import infoqoch.telegrambot.bot.DefaultTelegramBotFactory;
import infoqoch.telegrambot.bot.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class UpdateConfig {
    private final ApplicationContext context;

    @Bean
    public TelegramFileHandler telegramFileHandler(){
        return new TelegramFileHandler(telegramBot(), telegramProperties());
    }

    @Bean
    public CustomUpdateRequestReturnRegister customUpdateRequestReturnRegister(){
        List<UpdateRequestReturn> result = new ArrayList<>();

        final Set<Class<? extends CustomUpdateRequestReturnRegister>> subTypesOf = new Reflections(
                new ConfigurationBuilder().setUrls(generateTargetUrl(extractBasePackageName(context))).setScanners(Scanners.SubTypes)
        ).getSubTypesOf(CustomUpdateRequestReturnRegister.class);

        for (Class<? extends CustomUpdateRequestReturnRegister> aClass : subTypesOf) {
            try{
                final CustomUpdateRequestReturnRegister bean = context.getBean(aClass);
                result.addAll(bean.returnRegister());
                log.info("{} implements CustomUpdateRequestReturnRegister and added resolvers in UpdateRequestReturnRegister", aClass);
            }catch (Exception e){
                e.printStackTrace();
                log.info("{} implements CustomUpdateRequestReturnRegister but not bean", aClass);
            }
        }

        return () -> result;
    }

    @Bean
    public CustomUpdateRequestParamRegister customUpdateRequestParamRegister(){
        List<UpdateRequestParam> result = new ArrayList<>();

        final Set<Class<? extends CustomUpdateRequestParamRegister>> subTypesOf = new Reflections(
                new ConfigurationBuilder().setUrls(generateTargetUrl(extractBasePackageName(context))).setScanners(Scanners.SubTypes)
        ).getSubTypesOf(CustomUpdateRequestParamRegister.class);

        for (Class<? extends CustomUpdateRequestParamRegister> aClass : subTypesOf) {
            try{
                final CustomUpdateRequestParamRegister bean = context.getBean(aClass);
                result.addAll(bean.paramRegister());
                log.info("{} implements CustomUpdateRequestParamRegister and added resolvers in UpdateRequestParamRegister", aClass);
            }catch (Exception e){
                e.printStackTrace();
                log.info("{} implements CustomUpdateRequestParamRegister but not bean", aClass);
            }
        }

        return () -> result;
    }

    @Bean
    public TelegramProperties telegramProperties(){
        return TelegramProperties.generate();
    }

    @Bean
    public UpdateRequestReturnRegister updateRequestReturnRegister(){
        UpdateRequestReturnRegister updateRequestReturnRegister = new UpdateRequestReturnRegister();
        updateRequestReturnRegister.add(new MSBUpdateRequestReturn());
        updateRequestReturnRegister.add(new StringUpdateRequestReturn());
        updateRequestReturnRegister.add(new UpdateResponseUpdateRequestReturn());
        updateRequestReturnRegister.add(new VoidUpdateRequestReturn());

        for (UpdateRequestReturn updateRequestReturn : customUpdateRequestReturnRegister().returnRegister()) {
            updateRequestReturnRegister.add(updateRequestReturn);
        }

        return updateRequestReturnRegister;
    }

    @Bean
    public UpdateRequestParamRegister updateRequestParamRegister(){
        UpdateRequestParamRegister paramResolvers = new UpdateRequestParamRegister();
        paramResolvers.add(new UpdateRequestUpdateRequestParam());
        paramResolvers.add(new UpdateRequestMessageUpdateRequestParam());
        paramResolvers.add(new UpdateChatUpdateRequestParam());
        paramResolvers.add(new UpdateDocumentUpdateRequestParam());
        paramResolvers.add(new TelegramPropertiesRequestParam(telegramProperties()));

        for (UpdateRequestParam updateRequestParam : customUpdateRequestParamRegister().paramRegister()) {
            paramResolvers.add(updateRequestParam);
        }

        return paramResolvers;
    }

    @Bean
    public TelegramBot telegramBot(){
        return DefaultTelegramBotFactory.init(telegramProperties().token());
    }

    @Bean
    public UpdateDispatcher updateDispatcher(){
        final SpringBeanContext springContext = new SpringBeanContext(context);
        final Collection<URL> urls = generateTargetUrl(extractBasePackageName(context));
        final Map<UpdateRequestCommand, UpdateRequestMethodResolver> methodResolvers = UpdateRequestMethodResolverFactory.collectUpdateRequestMappedMethods(
                springContext
                , urls
                , updateRequestParamRegister()
                , updateRequestReturnRegister()
        );
        return new UpdateDispatcher(methodResolvers);
    }

    private static String extractBasePackageName(ApplicationContext context) {
        return context.getBeansWithAnnotation(EnableTelegramFramework.class).values().stream().findAny().get().getClass().getPackageName();
    }

    // TODO
    // 통합 테스트(@SpringBootTest)가 동작할 때 클래스로더가 두 개 동작하며 그것의 절대 경로는 ../target/test-classes 와 ../target/classes 이다.
    // 실제 스프링 컨테이너를 로딩할 때는, 테스트를 사용하지 않으므로 이러한 코드가 필요 없다. 테스트를 할 때는 두 개의 클래스로더가 동작하기 때문에 불가피하게 아래와 같이 "/test-classes"에 있는 파일을 읽지 않도록 한다.
    // 운영이 테스트를 위한 코드에 종속되는 것은 좋지 않아서 최대한 해소하려 하였으나 어쩔 수 없이 아래와 같이 코드를 작성하였음.
    // 이에 대한 개선책이 반드시 필요함.

    // TODO
    // 지금까지 url은 src/main src/test 로부터 시작한다.
    // 하지만 ComponentScan의 원칙은 해당 메서드를 기점으로부터 검색한다.
    // 이러한 부분을 내가 놓쳤고, 관련하여 URL 객체를 새로 구현토록 하였다.
    // 이 부분은 테스트 이후 어떤식으로 처리할지 확정한다.
    private Set<URL> generateTargetUrl(String basePackageName) {
        return ClasspathHelper.forPackage(basePackageName).stream()
//                .filter(url -> !url.toString().contains("/test-classes")) // maven
//                .filter(url -> !url.toString().contains("/test/classes")) // gradle
//                .filter(url -> !url.toString().contains("/java/test/")) // gradle test
                .map(u -> appendPackageName(u, basePackageName))
                .peek(u -> log.info("url = {}", u))
                .collect(Collectors.toSet());
    }

    private static URL appendPackageName(URL url, String packageName) {
        try {
            return new URL(url.toString() + packageName.replaceAll("[.]", "/"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
