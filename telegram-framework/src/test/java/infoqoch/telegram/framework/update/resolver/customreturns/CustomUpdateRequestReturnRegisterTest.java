package infoqoch.telegram.framework.update.resolver.customreturns;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import infoqoch.telegram.framework.update.resolver.custom.CustomUpdateRequestReturnRegister;
import infoqoch.telegram.framework.update.resolver.returns.MSBUpdateRequestReturn;
import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturn;
import infoqoch.telegram.framework.update.resolver.returns.UpdateRequestReturnRegister;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CustomUpdateRequestReturnRegisterTest {
    private UpdateRequestReturnRegister returnRegister;

    @BeforeEach
    void setUp(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(Config.class);
        returnRegister = ac.getBean(UpdateRequestReturnRegister.class);
    }

    @Test
    void custom_resolver(){
        // give
        final LocalDateTime now = LocalDateTime.now();
        final Sample target = new Sample(123l, now);

        MarkdownStringBuilder expected = new MarkdownStringBuilder()
                .bold("update id : [" + target.getUpdateId() + "]").lineSeparator()
                .italic("date : ").italic((target.getRegDt()).toString()).lineSeparator();

        // when
        final Optional<UpdateRequestReturn> resolver = returnRegister.support(target);

        //then
        Assertions.assertThat(resolver).isPresent();
        assertThat(resolver.get()).isInstanceOf(SampleUpdateRequestReturn.class);
        assertThat(resolver.get().resolve(target).getMessage().toString()).isEqualTo(expected.toString());
    }

    @Test
    void default_resolver(){
        // give
        final MarkdownStringBuilder target = new MarkdownStringBuilder("hi!!");

        // when
        final Optional<UpdateRequestReturn> resolver = returnRegister.support(target);

        //then
        Assertions.assertThat(resolver).isPresent();
        assertThat(resolver.get()).isInstanceOf(MSBUpdateRequestReturn.class);
        assertThat(resolver.get().resolve(target).getMessage().toString()).isEqualTo(target.toString());
    }

    @Configuration
    @ComponentScan
    @EnableTelegramFramework
    static class Config implements CustomUpdateRequestReturnRegister {
        @Bean
        public AnyHandler sampleHandler() {
            return new AnyHandler();
        }

        @Override
        public List<UpdateRequestReturn> returnRegister() {
            return Arrays.asList(new SampleUpdateRequestReturn());
        }
    }
}