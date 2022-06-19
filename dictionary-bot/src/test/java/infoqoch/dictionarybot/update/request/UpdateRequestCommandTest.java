package infoqoch.dictionarybot.update.request;

import org.junit.jupiter.api.Test;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.*;
import static org.assertj.core.api.Assertions.assertThat;

class UpdateRequestCommandTest {
    @Test
    void resolve_command(){
        assertMatchCommand("help", "help", HELP);
        assertMatchCommand("excel push", "excel push", EXCEL_PUSH);
        assertMatchCommand("w", "w", LOOKUP_WORD);

        assertNotMatchCommand("wefwef",  UNKNOWN);
        assertNotMatchCommand("/help",  UNKNOWN);
        assertNotMatchCommand("he lp",  UNKNOWN);
        assertNotMatchCommand("help ",  HELP);
        assertNotMatchCommand("excel_push",  UNKNOWN);
    }

    private void assertNotMatchCommand(String input, UpdateRequestCommand expectedCommand) {
        // final Optional<String> contains = UpdateRequestCommand.contains(input);
        // assertThat(contains).isNotPresent();
        final UpdateRequestCommand command = UpdateRequestCommand.of(input);
        assertThat(command).isEqualTo(expectedCommand);
    }

    private void assertMatchCommand(String input, String actual,UpdateRequestCommand expectedCommand) {
//        final Optional<String> contains = UpdateRequestCommand.contains(input);
//        assertThat(contains).isPresent();
//        assertThat(contains.get()).isEqualTo(actual);
        final UpdateRequestCommand command = UpdateRequestCommand.of(input);
        assertThat(command).isEqualTo(expectedCommand);
    }
}