package infoqoch.dictionarybot.update.resolver.returns;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateRequestReturnListTest {
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
        assertThat(resolver.get().resolve(target).message()).usingRecursiveComparison().isEqualTo(target);
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
        assertThat(resolver.get().resolve(target).message()).usingRecursiveComparison().isEqualTo(new MarkdownStringBuilder(target));
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
        assertThat(resolver.get().resolve(target).message()).usingRecursiveComparison().isEqualTo(target.toMarkdown());
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
        assertThat(resolver.get().resolve(target).message().text()).contains(d1.toMarkdown().text());
        assertThat(resolver.get().resolve(target).message().text()).contains(d2.toMarkdown().text());
    }

    private Dictionary createSimpleDictionary(DictionaryContent content, long no) {
        return Dictionary.builder().no(no).content(content).build();
    }

    private DictionaryContent createSimpleDictionaryContent() {
        DictionaryContent content = DictionaryContent.builder()
                .word("apple")
                .pronunciation("애포얼")
                .partOfSpeech("noun")
                .source("아낌없이 주는 나무")
                .definition("사과")
                .sentence("Iphone 7 is the latest model")
                .build();
        return content;
    }

}