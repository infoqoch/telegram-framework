package infoqoch.dictionarybot.update.request.body;

import com.fasterxml.jackson.core.JsonProcessingException;
import infoqoch.dictionarybot.update.request.UpdateRequestMessage;
import infoqoch.dictionarybot.update.request.UpdateRequestCommand;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.JsonBind;
import org.junit.jupiter.api.Test;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.*;
import static infoqoch.dictionarybot.mock.data.MockUpdate.*;
import static org.assertj.core.api.Assertions.assertThat;

class UpdateRequestMessageBodyParsingTest {
    JsonBind jsonBind = new DefaultJsonBind();

    // MockUpdateJsonGenerate에 사용한 raw json이 아래의 데이터임.
    @Test
    void resolve_type() throws JsonProcessingException {
        String chat_json = "{\"ok\":true,\"result\":[{\"update_id\":567841804,\n" +
                "\"message\":{\"message_id\":2102,\"from\":{\"id\":39327045,\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652025791,\"text\":\"hi\"}}]}";
        assertType(chat_json, UpdateType.CHAT);

        String document_json = "{\"ok\": true,\"result\": [{\"update_id\": 567841807,\"message\": {\"message_id\": 2149,\"from\": {\"id\": 39327045,\"is_bot\": false,\"first_name\": \"\\uc11d\\uc9c4\",\"language_code\": \"ko\"},\"chat\": {\"id\": 39327045,\"first_name\": \"\\uc11d\\uc9c4\",\"type\": \"private\"},\"date\": 1653482399,\"document\": {\"file_name\": \"test.txt\",\"mime_type\": \"text/plain\",\"file_id\": \"BQACAgUAAxkBAAIIZWKOI5-9F9n8O1nh5Bz2m505K7qfAAI5BgACNKpwVL13NImKS4jvJAQ\",\"file_unique_id\": \"AgADOQYAAjSqcFQ\",\"file_size\": 8},\"caption\": \"send test document\"}}]}";
        assertType(document_json, UpdateType.DOCUMENT);

        String photo_json = "{\"ok\": true,\"result\": [{\"update_id\": 567841808,\"message\": {\"message_id\": 2150,\"from\": {\"id\": 39327045,\"is_bot\": false,\"first_name\": \"\\uc11d\\uc9c4\",\"language_code\": \"ko\"},\"chat\": {\"id\": 39327045,\"first_name\": \"\\uc11d\\uc9c4\",\"type\": \"private\"},\"date\": 1653482413,\"photo\": [{\"file_id\": \"AgACAgUAAxkBAAIIZmKOI6wEb4PQtzRFKkLv8fPtja6tAAJYsTEbNKpwVIdigkCEtD4HAQADAgADcwADJAQ\",\"file_unique_id\": \"AQADWLExGzSqcFR4\",\"file_size\": 1196,\"width\": 90,\"height\": 90},{\"file_id\": \"AgACAgUAAxkBAAIIZmKOI6wEb4PQtzRFKkLv8fPtja6tAAJYsTEbNKpwVIdigkCEtD4HAQADAgADbQADJAQ\",\"file_unique_id\": \"AQADWLExGzSqcFRy\",\"file_size\": 15474,\"width\": 320,\"height\": 320},{\"file_id\": \"AgACAgUAAxkBAAIIZmKOI6wEb4PQtzRFKkLv8fPtja6tAAJYsTEbNKpwVIdigkCEtD4HAQADAgADeAADJAQ\",\"file_unique_id\": \"AQADWLExGzSqcFR9\",\"file_size\": 52057,\"width\": 800,\"height\": 800},{\"file_id\": \"AgACAgUAAxkBAAIIZmKOI6wEb4PQtzRFKkLv8fPtja6tAAJYsTEbNKpwVIdigkCEtD4HAQADAgADeQADJAQ\",\"file_unique_id\": \"AQADWLExGzSqcFR-\",\"file_size\": 70180,\"width\": 1280,\"height\": 1280}],\"caption\": \"photo test\"}}]}";
        assertType(photo_json, UpdateType.PHOTO);
    }

    private void assertType(String json, UpdateType type) {
        UpdateRequest wrapper = jsonToUpdateWrapper(json);
        assertThat(wrapper.type()).isEqualTo(type);
    }

    @Test
    void extractCommand(){
        assertUpdateRequest(jsonToUpdateWrapper(chatJson("/help")).updateRequestMessage(), HELP, "");
        assertUpdateRequest(jsonToUpdateWrapper(chatJson("/help_hi")).updateRequestMessage(), HELP, "hi");
        assertUpdateRequest(jsonToUpdateWrapper(chatJson("/w_hi")).updateRequestMessage(), LOOKUP_WORD, "hi");
        assertUpdateRequest(jsonToUpdateWrapper(documentJson("/w_hi")).updateRequestMessage(), LOOKUP_WORD, "hi");
        assertUpdateRequest(jsonToUpdateWrapper(photoJson("/w_hi")).updateRequestMessage(), LOOKUP_WORD, "hi");
        assertUpdateRequest(jsonToUpdateWrapper(documentJson("/excel_push")).updateRequestMessage(), EXCEL_PUSH, "");
    }

    private void assertUpdateRequest(UpdateRequestMessage request, UpdateRequestCommand command, String value) {
        assertThat(request.command()).isEqualTo(command);
        assertThat(request.value()).isEqualTo(value);
    }

    @Test
    void handle_spaces_and_tabs(){
        assertMultipleSpaceAndTabReplaceWithSingleSpace("abc     efg", "abc efg");
        assertMultipleSpaceAndTabReplaceWithSingleSpace("abc         efg", "abc efg");
        assertMultipleSpaceAndTabReplaceWithSingleSpace("abc                      efg", "abc efg");
        assertMultipleSpaceAndTabReplaceWithSingleSpace("      a         b    c     e           f                  g", "a b c e f g");
    }

    private void assertMultipleSpaceAndTabReplaceWithSingleSpace(String actual, String expected) {
        String regex = "[\\t\\s]+";
        final String result = actual.replaceAll(regex, " ").trim();
        assertThat(result).isEqualTo(expected);
    }
}