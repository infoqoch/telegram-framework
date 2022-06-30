package infoqoch.dictionarybot.bot;

import infoqoch.dictionarybot.send.SendDispatcher;
import infoqoch.dictionarybot.update.UpdateDispatcher;
import infoqoch.dictionarybot.update.controller.resolver.bean.SpringBeanContext;
import infoqoch.dictionarybot.update.controller.resolver.returns.*;
import infoqoch.telegrambot.bot.DefaultTelegramBotFactory;
import infoqoch.telegrambot.bot.TelegramBot;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Configuration
public class DictionaryBotConfig {

    @Bean
    TelegramBot telegramBot(){
        return DefaultTelegramBotFactory.init("1959903402:AAFfvMCssvDcESLewCDvj5WZk83cbnIZ08o");
    }

    @Bean
    public List<UpdateRequestReturn> returnResolvers(){
        log.info("good!");
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
    public UpdateDispatcher updateDispatcher(ApplicationContext context){
        final Collection<URL> urls = getUrlsExcludeTest();
        return new UpdateDispatcher(new SpringBeanContext(context), urls, returnResolvers());
    }

    // 통합 테스트(@SpringBootTest)가 동작할 때 클래스로더가 두 개 동작하며 그것의 절대 경로는 ../target/test-classes 와 ../target/classes 이다.
    // 운영이 테스트를 위한 코드에 종속되는 것은 좋지 않아서 최대한 해소하려 하였으나 어쩔 수 없이 아래와 같이 코드를 작성하였음.
    // DictionaryBotApplication 를 기준으로 url을 추출하였으나 이 경우 /test-classes 의 패키지로 url을 가짐.
    // 이 부분은 계속 고민이 필요해보임.
    private Set<URL> getUrlsExcludeTest() {
        return ClasspathHelper.forPackage(UpdateDispatcher.class.getPackageName()).stream().filter(url -> !url.toString().contains("/test-classes")).collect(Collectors.toSet());
    }

    @Bean
    public SendDispatcher sendDispatcher(){
        return new SendDispatcher(telegramBot());
    }

}
