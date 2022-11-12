package infoqoch.telegram.framework.update.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateRequestCommandSplitTest {
    @Test
    void empty(){
        assertThat(UpdateRequestCommandSplit.flattingInput("")).isEqualTo("");
    }
}