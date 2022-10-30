package infoqoch.dictionarybot.update.request;

import infoqoch.dictionarybot.mock.data.MockUpdate;
import infoqoch.dictionarybot.update.request.body.UpdateChat;
import infoqoch.dictionarybot.update.request.body.UpdateDocument;
import infoqoch.telegrambot.bot.entity.Update;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateRequestMessageBodyTest {
    @DisplayName("document로 전달한 telegram의 raw json을 Update와 UpdateDocument로 정상 변환됨을 확인한다.")
    @Test
    void wrapper_to_chat_request() {
        final Update update = MockUpdate.jsonToUpdate(MockUpdate.chatJson("/help"));
        final UpdateChat chat = MockUpdate.jsonToUpdateRequest(MockUpdate.chatJson("/help")).toChat();

        assertThat(chat.getUpdateId()).isEqualTo(update.getUpdateId());
        assertThat(chat.getMessageId()).isEqualTo(update.getMessage().getMessageId());
        assertThat(chat.getDate()).isEqualTo(update.getMessage().getDate());
        assertThat(chat.getText()).isEqualTo(update.getMessage().getText());
        assertThat(chat.getFrom()).usingRecursiveComparison().isEqualTo(update.getMessage().getFrom());
        assertThat(chat.getChat()).usingRecursiveComparison().isEqualTo(update.getMessage().getChat());
    }

    @DisplayName("chat로 전달한 telegram의 raw json을 Update와 UpdateChat으로 정상 변환됨을 확인한다.")
    @Test
    void wrapper_to_document_request() {
        final Update update = MockUpdate.jsonToUpdate(MockUpdate.documentJson("/help"));
        final UpdateDocument document = MockUpdate.jsonToUpdateRequest(MockUpdate.documentJson("/help")).toDocument();

        assertThat(document.getUpdateId()).isEqualTo(update.getUpdateId());
        assertThat(document.getMessageId()).isEqualTo(update.getMessage().getMessageId());
        assertThat(document.getDate()).isEqualTo(update.getMessage().getDate());
        assertThat(document.getCaption()).isEqualTo(update.getMessage().getCaption());
        assertThat(document.getDocument()).usingRecursiveComparison().isEqualTo(update.getMessage().getDocument());
        assertThat(document.getFrom()).usingRecursiveComparison().isEqualTo(update.getMessage().getFrom());
        assertThat(document.getChat()).usingRecursiveComparison().isEqualTo(update.getMessage().getChat());
    }
}
