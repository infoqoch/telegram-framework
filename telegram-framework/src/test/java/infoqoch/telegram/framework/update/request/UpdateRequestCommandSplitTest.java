package infoqoch.telegram.framework.update.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// UpdateRequestCommand 에서 테스트 대부분 진행하여 
// 예외 상황에 대해서만 테스트 진행
// TODO null에 대해서 예외 처리를 해야 하는가? empty string을 전달해야 하는가? 음.
class UpdateRequestCommandSplitTest {
    
    @Test
    void empty(){
        assertThat(UpdateRequestCommandSplit.flattingInput("")).isEqualTo("");
    }

}