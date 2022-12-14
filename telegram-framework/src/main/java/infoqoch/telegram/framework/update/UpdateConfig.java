package infoqoch.telegram.framework.update;

import infoqoch.telegram.framework.update.event.Events;
import infoqoch.telegram.framework.update.file.TelegramFileHandler;
import infoqoch.telegram.framework.update.request.UpdateRequestCommand;
import infoqoch.telegram.framework.update.resolver.custom.CustomUpdateRequestParamRegister;
import infoqoch.telegram.framework.update.resolver.custom.CustomUpdateRequestReturnRegister;
import infoqoch.telegram.framework.update.resolver.param.*;
import infoqoch.telegram.framework.update.resolver.returns.*;
import infoqoch.telegram.framework.update.send.SendEventListener;
import infoqoch.telegram.framework.update.util.ReflectionUtil;
import infoqoch.telegrambot.bot.DefaultTelegramBotFactory;
import infoqoch.telegrambot.bot.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;


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

        String basePackage = frameworkBase().get().getClass().getPackage().getName();

        ConfigurationBuilder configuration = new ConfigurationBuilder()
                .forPackage(basePackage)
                .setScanners(Scanners.SubTypes);
        ReflectionUtil.ifJarThenCalibrating(configuration, basePackage);

        final Set<Class<? extends CustomUpdateRequestReturnRegister>> registers =
                new Reflections(configuration).getSubTypesOf(CustomUpdateRequestReturnRegister.class);

        for (Class<? extends CustomUpdateRequestReturnRegister> register : registers) {
            try{
                final CustomUpdateRequestReturnRegister bean = context.getBean(register);
                result.addAll(bean.returnRegister());
                log.info("{} implements CustomUpdateRequestReturnRegister and added resolvers in UpdateRequestReturnRegister", register);
            }catch (Exception e){
                e.printStackTrace();
                log.info("{} implements CustomUpdateRequestReturnRegister but not bean", register);
            }
        }

        return () -> result;
    }

    @Bean
    public CustomUpdateRequestParamRegister customUpdateRequestParamRegister(){
        List<UpdateRequestParam> result = new ArrayList<>();

        String basePackage = frameworkBase().get().getClass().getPackage().getName();

        ConfigurationBuilder configuration = new ConfigurationBuilder()
                .forPackage(basePackage)
                .setScanners(Scanners.SubTypes);
        ReflectionUtil.ifJarThenCalibrating(configuration, basePackage);

        final Set<Class<? extends CustomUpdateRequestParamRegister>> registers =
                new Reflections(configuration).getSubTypesOf(CustomUpdateRequestParamRegister.class);

        for (Class<? extends CustomUpdateRequestParamRegister> register : registers) {
            try{
                final CustomUpdateRequestParamRegister bean = context.getBean(register);
                result.addAll(bean.paramRegister());
                log.info("{} implements CustomUpdateRequestParamRegister and added resolvers in UpdateRequestParamRegister", register);
            }catch (Exception e){
                e.printStackTrace();
                log.info("{} implements CustomUpdateRequestParamRegister but not bean", register);
            }
        }
        return () -> result;
    }

    @Bean
    public TelegramProperties telegramProperties(){
        return TelegramProperties.generate("telegram-framework.properties");
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
        final Map<UpdateRequestCommand, UpdateRequestResolver> methodResolvers = UpdateRequestMapperFactory.collectUpdateRequestMappedMethods(
                context
                , frameworkBase().get()
                , updateRequestParamRegister()
                , updateRequestReturnRegister()
        );
        return new UpdateDispatcher(methodResolvers);
    }

    @Bean
    public UpdateRunner updateRunner(){
        return new UpdateRunner(telegramBot(), updateDispatcher());
    }

    @Bean
    public InitializingBean eventsInitializer() {
        return () -> Events.setPublisher(context);
    }

    @Bean
    public SendEventListener sendUpdateResponseEventListener(){
        if(telegramProperties().sendMessageAfterUpdateResolved()){
            return new SendEventListener(telegramBot().send());
        }
        return null;
    }

    private Optional<Object> frameworkBase() {
        return context.getBeansWithAnnotation(EnableTelegramFramework.class).values().stream().findAny();
    }
}
