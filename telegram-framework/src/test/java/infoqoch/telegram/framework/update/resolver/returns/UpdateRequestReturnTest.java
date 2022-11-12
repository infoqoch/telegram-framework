package infoqoch.telegram.framework.update.resolver.returns;

import infoqoch.telegram.framework.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateRequestReturnTest {
    @Test
    void string(){
        assertString("반가워", "반가워");
        assertString("반가워!", "반가워\\!");
    }

    private void assertString(String body, String expected) {
        // given
        UpdateRequestReturn resolver = new StringUpdateRequestReturn();

        // support
        boolean isSupport = resolver.support(body);
        assertThat(isSupport).isTrue();

        // resolve
        UpdateResponse result =  resolver.resolve(body);
        assertThat(result.getMessage().toString()).isEqualTo(expected);
    }

    @Test
    void markdownStringBuilder(){
        assertMSB(new MarkdownStringBuilder().plain("반갑습니다."), "반갑습니다\\.");
        assertMSB(new MarkdownStringBuilder().plain("반갑습니다.").code("<h3>hi</h3>"), "반갑습니다\\.`\\<h3\\>hi\\<\\/h3\\>`");
    }

    private void assertMSB(MarkdownStringBuilder target, String expected) {
        // given
        UpdateRequestReturn resolver = new MSBUpdateRequestReturn();

        // support
        boolean isSupport = resolver.support(target);
        assertThat(isSupport).isTrue();

        // resolve
        UpdateResponse result =  resolver.resolve(target);
        assertThat(result.getMessage().toString()).isEqualTo(target.toString());
        assertThat(result.getMessage().toString()).isEqualTo(expected);
    }
}
