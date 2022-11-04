package infoqoch.telegram.framework.update.resolver.returns;

import infoqoch.telegram.framework.update.UpdateConfig;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateRequestReturnStreamTest {
    final List<UpdateRequestReturn> returnResolvers = new UpdateConfig().returnResolvers();

    @Test
    void markdownStringBuilder(){
        // give
        final MarkdownStringBuilder target = new MarkdownStringBuilder("hi!!");

        // when
        final Optional<UpdateRequestReturn> resolver = returnResolvers.stream().filter(r -> r.support(target)).findAny();

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
        final Optional<UpdateRequestReturn> resolver = returnResolvers.stream().filter(r -> r.support(target)).findAny();

        //then
        Assertions.assertThat(resolver).isPresent();
        assertThat(resolver.get()).isInstanceOf(StringUpdateRequestReturn.class);
        assertThat(resolver.get().resolve(target).getMessage()).usingRecursiveComparison().isEqualTo(new MarkdownStringBuilder(target));
    }

}