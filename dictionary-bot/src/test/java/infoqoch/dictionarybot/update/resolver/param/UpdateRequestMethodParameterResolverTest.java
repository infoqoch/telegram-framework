package infoqoch.dictionarybot.update.resolver.param;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import infoqoch.dictionarybot.update.resolver.returns.DictionaryUpdateRequestMethodParameterResolver;
import infoqoch.dictionarybot.update.resolver.returns.MSBUpdateRequestMethodParameterResolver;
import infoqoch.dictionarybot.update.resolver.returns.StringUpdateRequestMethodParameterResolver;
import infoqoch.dictionarybot.update.resolver.returns.UpdateRequestMethodParameterResolver;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateRequestMethodParameterResolverTest {

    @Test
    void string(){
        assertString("반가워", "반가워");
        assertString("반가워!", "반가워\\!");
    }

    private void assertString(String body, String expected) {
        // given
        UpdateRequestMethodParameterResolver resolver = new StringUpdateRequestMethodParameterResolver();

        // support
        boolean isSupport = resolver.support(body);
        assertThat(isSupport).isTrue();

        // resolve
        MarkdownStringBuilder result =  resolver.resolve(body);
        assertThat(result.toString()).isEqualTo(expected);
    }

    @Test
    void markdownBuilder(){
        assertMSB(new MarkdownStringBuilder().plain("반갑습니다."), "반갑습니다\\.");
        assertMSB(new MarkdownStringBuilder().plain("반갑습니다.").code("<h3>hi</h3>"), "반갑습니다\\.`\\<h3\\>hi\\<\\/h3\\>`");
    }

    private void assertMSB(MarkdownStringBuilder target, String expected) {
        // given
        UpdateRequestMethodParameterResolver resolver = new MSBUpdateRequestMethodParameterResolver();

        // support
        boolean isSupport = resolver.support(target);
        assertThat(isSupport).isTrue();

        // resolve
        MarkdownStringBuilder result =  resolver.resolve(target);
        assertThat(result).usingRecursiveComparison().isEqualTo(target);
        assertThat(result.toString()).isEqualTo(expected);
    }

    @Test
    void test(){
        final Dictionary simpleDictionary = createSimpleDictionary(createSimpleDictionaryContent(), 123l);
        System.out.println("simpleDictionary = " + simpleDictionary.toString());

    }

    @Test
    void single_dictionary(){
        final Dictionary simpleDictionary = createSimpleDictionary(createSimpleDictionaryContent(), 123l);
        assertSingleDictionary(simpleDictionary, "*apple*\\(애포얼\\)", "_사과_, Iphone 7 is the latest model");
    }

    private void assertSingleDictionary(Dictionary target, String...expected) {
        // given
        UpdateRequestMethodParameterResolver resolver = new DictionaryUpdateRequestMethodParameterResolver();

        // support
        boolean isSupport = resolver.support(target);
        assertThat(isSupport).isTrue();

        // resolve
        MarkdownStringBuilder result =  resolver.resolve(target);
        assertThat(result.toString()).contains(expected);
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