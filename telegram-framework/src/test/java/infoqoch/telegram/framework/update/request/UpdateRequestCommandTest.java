package infoqoch.telegram.framework.update.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UpdateRequestCommandTest {
    @Test
    void space_and_underbar(){
        assertExtractingValueByCommand("hi", "/hi goodday", "goodday");
        assertExtractingValueByCommand("hi", "hi goodday", "goodday");
        assertExtractingValueByCommand("hi", "hi_goodday", "goodday");
        assertExtractingValueByCommand("hi", "/hi_goodday", "goodday");
    }

    @Test
    void complex_value(){
        assertExtractingValueByCommand("hi", "/hi good_day", "good day");
        assertExtractingValueByCommand("hi", "/hi good day", "good day");
        assertExtractingValueByCommand("hi", "/hi_good_day", "good day");
        assertExtractingValueByCommand("hi", "/hi good_day", "good day");
    }

    @Test
    void complex_command(){
        assertExtractingValueByCommand("good day", "good day hello world", "hello world");
        assertExtractingValueByCommand("good day", "/good_day hello world", "hello world");
        assertExtractingValueByCommand("good day", "/good day_hello world", "hello world");
        assertExtractingValueByCommand("good day", "good_day_hello_world", "hello world");
    }

    @Test
    void invalid(){
        assertThatThrownBy(()->
                UpdateRequestCommand.of("")
        ).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(()->
                UpdateRequestCommand.of(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private static void assertExtractingValueByCommand(String command, String actualMessage, String expectedValue) {
        assertThat(UpdateRequestCommand.of(command).extractValue(actualMessage)).isEqualTo(expectedValue);
    }
}