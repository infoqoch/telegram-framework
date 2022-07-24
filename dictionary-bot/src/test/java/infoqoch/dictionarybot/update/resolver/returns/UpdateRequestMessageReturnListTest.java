package infoqoch.dictionarybot.update.resolver.returns;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.update.controller.resolver.returns.*;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static infoqoch.dictionarybot.mock.data.MockDictionary.createSimpleDictionary;
import static infoqoch.dictionarybot.mock.data.MockDictionary.createSimpleDictionaryContent;
import static org.assertj.core.api.Assertions.assertThat;

class UpdateRequestMessageReturnListTest {
    List<UpdateRequestReturn> returnResolvers;

    @BeforeEach
    void setUp(){
        returnResolvers = new ArrayList<>();
        returnResolvers.add(new DictionaryUpdateRequestReturn());
        returnResolvers.add(new MSBUpdateRequestReturn());
        returnResolvers.add(new StringUpdateRequestReturn());
        returnResolvers.add(new DictionariesUpdateRequestReturn());
    }

    @Test
    void msb(){
        // give
        final MarkdownStringBuilder target = new MarkdownStringBuilder("hi!!");

        // when
        final Optional<UpdateRequestReturn> resolver = returnResolvers.stream().filter(r -> r.support(target)).findAny();

        //then
        assertThat(resolver).isPresent();
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
        assertThat(resolver).isPresent();
        assertThat(resolver.get()).isInstanceOf(StringUpdateRequestReturn.class);
        assertThat(resolver.get().resolve(target).getMessage()).usingRecursiveComparison().isEqualTo(new MarkdownStringBuilder(target));
    }

    @Test
    void dictionary(){
        // give
        final Dictionary target = createSimpleDictionary(createSimpleDictionaryContent(), 123);

        // when
        final Optional<UpdateRequestReturn> resolver = returnResolvers.stream().filter(r -> r.support(target)).findAny();

        //then
        assertThat(resolver).isPresent();
        assertThat(resolver.get()).isInstanceOf(DictionaryUpdateRequestReturn.class);
        assertThat(resolver.get().resolve(target).getMessage()).usingRecursiveComparison().isEqualTo(target.toMarkdown());
    }

    @Test
    void dictionaries(){
        // give
        final Dictionary d1 = createSimpleDictionary(createSimpleDictionaryContent(), 123);
        final Dictionary d2 = createSimpleDictionary(createSimpleDictionaryContent(), 123);
        List<Dictionary> target = new ArrayList<>();
        target.add(d1);
        target.add(d2);

        // when
        final Optional<UpdateRequestReturn> resolver = returnResolvers.stream().filter(r -> r.support(target)).findAny();

        //then
        assertThat(resolver).isPresent();
        assertThat(resolver.get()).isInstanceOf(DictionariesUpdateRequestReturn.class);
        assertThat(resolver.get().resolve(target).getMessage().toString()).contains(d1.toMarkdown().toString());
        assertThat(resolver.get().resolve(target).getMessage().toString()).contains(d2.toMarkdown().toString());
    }
}