package infoqoch.dictionarybot.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DictionaryRequestFactoryTest {
    @Test
    void basic() {
        // 기본적인 추출 
        assertResolveMessage("/help", DictionaryCommand.HELP, "");
        assertResolveMessage("/help good", DictionaryCommand.HELP, "good");
        assertResolveMessage("/help good good", DictionaryCommand.HELP, "good good");
        
        // 모르는 명령
        assertResolveMessage("/wefijeif", DictionaryCommand.UNKNOWN, "");

        // 특수문자 대응
        // 명령은 첫 번째 스페이스 혹은 언더바를 기준으로 분리된다.
        // 모든 언더바는 스페이스로 변환된다.
        assertResolveMessage("/help_happy", DictionaryCommand.HELP, "happy");
        assertResolveMessage("help_happy", DictionaryCommand.HELP, "happy");
        assertResolveMessage("help happy", DictionaryCommand.HELP, "happy");
        assertResolveMessage("help good_job", DictionaryCommand.HELP, "good job");
        assertResolveMessage("/help_good_job", DictionaryCommand.HELP, "good job");
        assertResolveMessage("/help_good job", DictionaryCommand.HELP, "good job");
        assertResolveMessage("help good job!", DictionaryCommand.HELP, "good job!");
        assertResolveMessage("help !good 😀job^^", DictionaryCommand.HELP, "!good 😀job^^");
        
    }

    private void assertResolveMessage(String message, DictionaryCommand command, String value) {
        DictionaryRequest request = DictionaryRequestFactory.resolve(message);
        assertThat(request.command()).isEqualTo(command);
        assertThat(request.value()).isEqualTo(value);
    }
}
