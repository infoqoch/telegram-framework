package infoqoch.telegram.framework.update.resolver.returns;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import infoqoch.telegram.framework.update.UpdateConfig;
import infoqoch.telegram.framework.update.UpdateRequestMapper;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateRequestReturnRegisterTest {
    UpdateRequestReturnRegister returnRegister = new UpdateConfig(new AnnotationConfigApplicationContext(Config.class)).updateRequestReturnRegister();

    @Test
    void not_supported(){
        final LocalDateTime now = LocalDateTime.now();

        final Optional<UpdateRequestReturn> support = returnRegister.support(now);

        assertThat(support).isNotPresent();
    }

    @Test
    void markdownStringBuilder(){
        // give
        final MarkdownStringBuilder target = new MarkdownStringBuilder("hi!!");

        // when
        final Optional<UpdateRequestReturn> resolver = returnRegister.support(target);

        //then
        Assertions.assertThat(resolver).isPresent();
        assertThat(resolver.get()).isInstanceOf(MSBUpdateRequestReturn.class);
        assertThat(resolver.get().resolve(target).getMessage().toString()).isEqualTo(target.toString());
    }

    @Test
    void string(){
        // give
        final String target = "good day!";

        // when
        final Optional<UpdateRequestReturn> resolver = returnRegister.support(target);

        //then
        Assertions.assertThat(resolver).isPresent();
        assertThat(resolver.get()).isInstanceOf(StringUpdateRequestReturn.class);
        assertThat(resolver.get().resolve(target).getMessage().toString()).isEqualTo(new MarkdownStringBuilder(target).toString());
    }

    @Configuration
    @EnableTelegramFramework
    static class Config{
        @UpdateRequestMapper("*")
        public String any(){
            return "any";
        }
    }
}