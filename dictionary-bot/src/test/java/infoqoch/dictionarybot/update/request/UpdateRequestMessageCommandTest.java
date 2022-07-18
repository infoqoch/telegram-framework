package infoqoch.dictionarybot.update.request;

import org.junit.jupiter.api.Test;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.*;
import static org.assertj.core.api.Assertions.assertThat;

class UpdateRequestMessageCommandTest {
    @Test
    void resolve_command(){
        assertMatchCommand("help", HELP);
        assertMatchCommand("excel push", EXCEL_PUSH);
        assertMatchCommand("w", LOOKUP_WORD);

        assertNotMatchCommand("wefwef",  UNKNOWN);
        assertNotMatchCommand("/help",  UNKNOWN);
        assertNotMatchCommand("he lp",  UNKNOWN);
        assertNotMatchCommand("help ",  HELP);
        assertNotMatchCommand("excel_push",  UNKNOWN);
    }

    @Test
    void resolve_command_with_alias(){
        assertMatchCommand("ㄷ", LOOKUP_WORD);
        assertMatchCommand("ㅁ", LOOKUP_SENTENCE);
        assertMatchCommand("ㅈ", LOOKUP_DEFINITION);
        assertMatchCommand("push", EXCEL_PUSH);
    }


    private void assertNotMatchCommand(String input, UpdateRequestCommand expectedCommand) {
        // final Optional<String> contains = UpdateRequestCommand.contains(input);
        // assertThat(contains).isNotPresent();
        final UpdateRequestCommand command = UpdateRequestCommand.of(input);
        assertThat(command).isEqualTo(expectedCommand);
    }

    private void assertMatchCommand(String input, UpdateRequestCommand expectedCommand) {
//        final Optional<String> contains = UpdateRequestCommand.contains(input);
//        assertThat(contains).isPresent();
//        assertThat(contains.get()).isEqualTo(actual);
        final UpdateRequestCommand command = UpdateRequestCommand.of(input);
        assertThat(command).isEqualTo(expectedCommand);
    }
}