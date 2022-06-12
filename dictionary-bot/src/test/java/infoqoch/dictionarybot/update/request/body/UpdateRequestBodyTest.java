package infoqoch.dictionarybot.update.request.body;

import infoqoch.telegrambot.bot.entity.Update;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateRequestBodyTest {
    @Test
    void wrapper_to_chat_request() {
        final Update update = MockUpdateGenerate.jsonToUpdate(MockUpdateGenerate.chatJson("/help"));
        final UpdateChat chat = MockUpdateGenerate.jsonToUpdateWrapper(MockUpdateGenerate.chatJson("/help")).toChat();

        assertThat(chat.getUpdateId()).isEqualTo(update.getUpdateId());
        assertThat(chat.getMessageId()).isEqualTo(update.getMessage().getMessageId());
        assertThat(chat.getDate()).isEqualTo(update.getMessage().getDate());
        assertThat(chat.getText()).isEqualTo(update.getMessage().getText());
        assertThat(chat.getFrom()).usingRecursiveComparison().isEqualTo(update.getMessage().getFrom());
        assertThat(chat.getChat()).usingRecursiveComparison().isEqualTo(update.getMessage().getChat());
    }

    @Test
    void wrapper_to_document_request() {
        final Update update = MockUpdateGenerate.jsonToUpdate(MockUpdateGenerate.documentJson("/help"));
        final UpdateDocument document = MockUpdateGenerate.jsonToUpdateWrapper(MockUpdateGenerate.documentJson("/help")).toDocument();

        assertThat(document.getUpdateId()).isEqualTo(update.getUpdateId());
        assertThat(document.getMessageId()).isEqualTo(update.getMessage().getMessageId());
        assertThat(document.getDate()).isEqualTo(update.getMessage().getDate());
        assertThat(document.getCaption()).isEqualTo(update.getMessage().getCaption());
        assertThat(document.getDocument()).usingRecursiveComparison().isEqualTo(update.getMessage().getDocument());
        assertThat(document.getFrom()).usingRecursiveComparison().isEqualTo(update.getMessage().getFrom());
        assertThat(document.getChat()).usingRecursiveComparison().isEqualTo(update.getMessage().getChat());
    }
}
