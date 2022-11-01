package infoqoch.dictionarybot.update.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UpdateRequestCommandTest {
    @Test
    void test(){
        final UpdateRequestCommand command = UpdateRequestCommand.of("hi");
        assertThat(command.extractValue("/hi goodday")).isEqualTo("goodday");
    }

}