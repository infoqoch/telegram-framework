package infoqoch.dictionarybot.update.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static infoqoch.dictionarybot.mock.data.MockUpdate.*;
import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.*;
import static org.assertj.core.api.Assertions.assertThat;

class UpdateRequestMessageTest {
    @DisplayName("UpdateRequest가 UpdateRequestMessage으로 정상 변환됨을 확인한다.")
    @Test
    void extractCommand(){
        assertUpdateRequest(jsonToUpdateRequest(chatJson("/help")).updateRequestMessage(), HELP, "");
        assertUpdateRequest(jsonToUpdateRequest(chatJson("/help_hi")).updateRequestMessage(), HELP, "hi");
        assertUpdateRequest(jsonToUpdateRequest(chatJson("/w_hi")).updateRequestMessage(), LOOKUP_WORD, "hi");
        assertUpdateRequest(jsonToUpdateRequest(documentJson("/w_hi")).updateRequestMessage(), LOOKUP_WORD, "hi");
        assertUpdateRequest(jsonToUpdateRequest(documentJson("/excel_push")).updateRequestMessage(), EXCEL_PUSH, "");
        assertUpdateRequest(jsonToUpdateRequest(photoJson("/w_hi")).updateRequestMessage(), LOOKUP_WORD, "hi");
    }

    private void assertUpdateRequest(UpdateRequestMessage request, UpdateRequestCommand command, String value) {
        assertThat(request.getCommand()).isEqualTo(command);
        assertThat(request.getValue()).isEqualTo(value);
    }
}