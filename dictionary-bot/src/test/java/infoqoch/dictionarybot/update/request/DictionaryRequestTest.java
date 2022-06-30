package infoqoch.dictionarybot.update.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DictionaryRequestTest {
    @Test
    void resolve_command_value() {
        // 기본적인 추출 
        assertResolveMessage("/help", UpdateRequestCommand.HELP, "");
        assertResolveMessage("/help good", UpdateRequestCommand.HELP, "good");
        assertResolveMessage("/help good good", UpdateRequestCommand.HELP, "good good");
        
        // 모르는 명령
        // 모르는 명령어의 값은 텍스트 전체가 된다.
        assertResolveMessage("/wefijeif", UpdateRequestCommand.UNKNOWN, "wefijeif");
        assertResolveMessage("/helping_me", UpdateRequestCommand.UNKNOWN, "helping me");

        // 특수문자 대응
        // 명령은 첫 번째 스페이스 혹은 언더바를 기준으로 분리된다.
        // 모든 언더바는 스페이스로 변환된다.
        assertResolveMessage("/help_happy", UpdateRequestCommand.HELP, "happy");
        assertResolveMessage("help_happy", UpdateRequestCommand.HELP, "happy");
        assertResolveMessage("help happy", UpdateRequestCommand.HELP, "happy");
        assertResolveMessage("help good_job", UpdateRequestCommand.HELP, "good job");
        assertResolveMessage("/help_good_job", UpdateRequestCommand.HELP, "good job");
        assertResolveMessage("/help_good job", UpdateRequestCommand.HELP, "good job");
        assertResolveMessage("help good job!", UpdateRequestCommand.HELP, "good job!");
        assertResolveMessage("help !good 😀job^^", UpdateRequestCommand.HELP, "!good 😀job^^");
        
    }

    private void assertResolveMessage(String message, UpdateRequestCommand command, String value) {
        UpdateRequestMessage request = UpdateRequestFactory.resolve(message);
        assertThat(request.command()).isEqualTo(command);
        assertThat(request.value()).isEqualTo(value);
    }
}
