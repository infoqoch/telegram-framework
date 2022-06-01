package infoqoch.dictionarybot.request.update;

import infoqoch.telegrambot.bot.entity.Update;
import org.junit.jupiter.api.Test;

import static infoqoch.dictionarybot.request.update.MockUpdateJsonGenerate.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateWrapperToRequestTest {
    @Test
    void wrapper_to_chat_request() {
        final Update update = extractUpdate(mockChatJsonUpdate("/help"));
        final ChatRequest chat = resolveType(mockChatJsonUpdate("/help")).toChat();

        assertThat(chat.getUpdateId()).isEqualTo(update.getUpdateId());
        assertThat(chat.getMessageId()).isEqualTo(update.getMessage().getMessageId());
        assertThat(chat.getDate()).isEqualTo(update.getMessage().getDate());
        assertThat(chat.getText()).isEqualTo(update.getMessage().getText());
        assertThat(chat.getFrom()).usingRecursiveComparison().isEqualTo(update.getMessage().getFrom());
        assertThat(chat.getChat()).usingRecursiveComparison().isEqualTo(update.getMessage().getChat());
    }

    @Test
    void wrapper_to_document_request() {
        final Update update = extractUpdate(mockDocumentJsonUpdate("/help"));
        final DocumentRequest document = resolveType(mockDocumentJsonUpdate("/help")).toDocument();

        assertThat(document.getUpdateId()).isEqualTo(update.getUpdateId());
        assertThat(document.getMessageId()).isEqualTo(update.getMessage().getMessageId());
        assertThat(document.getDate()).isEqualTo(update.getMessage().getDate());
        assertThat(document.getCaption()).isEqualTo(update.getMessage().getCaption());
        assertThat(document.getDocument()).usingRecursiveComparison().isEqualTo(update.getMessage().getDocument());
        assertThat(document.getFrom()).usingRecursiveComparison().isEqualTo(update.getMessage().getFrom());
        assertThat(document.getChat()).usingRecursiveComparison().isEqualTo(update.getMessage().getChat());
    }
}
