package infoqoch.telegram.framework.update.resolver.returns;

import infoqoch.telegram.framework.update.UpdateConfig;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateRequestReturnRegisterTest {
    UpdateRequestReturnRegister returnRegister = new UpdateConfig().updateRequestReturnRegister(Collections.emptyList());

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
        assertThat(resolver.get().resolve(target).getMessage()).usingRecursiveComparison().isEqualTo(target);
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
        assertThat(resolver.get().resolve(target).getMessage()).usingRecursiveComparison().isEqualTo(new MarkdownStringBuilder(target));
    }
}