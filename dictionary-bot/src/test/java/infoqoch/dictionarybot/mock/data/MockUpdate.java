package infoqoch.dictionarybot.mock.data;

import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import infoqoch.telegrambot.util.DefaultJsonBind;

import java.util.List;

public class MockUpdate {
    public static Response<List<Update>> responseWithSingleChat(String text, long chatId) {
        return jsonToResponse(chatJson(text, chatId)) ;
    }

    public static UpdateRequest jsonToUpdateWrapper(String json) {
        return new UpdateRequest(jsonToUpdate(json));
    }

    public static Update jsonToUpdate(String json) {
        return jsonToResponse(json).getResult().get(0);
    }

    public static String chatJson(String text) {
        return chatJson(text, 12354235l) ;
    }

    private static Response<List<Update>> jsonToResponse(String json) {
        return new DefaultJsonBind().toList(json, Update.class);
    }

    public static String chatJson(String text, long chatId) {
        return "{\"ok\":true,\"result\":[{\"update_id\":567841804,\n" +
                "\"message\":{\"message_id\":2102,\"from\":{\"id\":" + chatId + ",\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652025791,\"text\":\"" + text + "\"}}]}";
    }

    public static String documentJson(String caption) {
        return "{\"ok\": true,\"result\": [{\"update_id\": 567841807,\"message\": {\"message_id\": 2149,\"from\": {\"id\": 39327045,\"is_bot\": false,\"first_name\": \"\\uc11d\\uc9c4\",\"language_code\": \"ko\"},\"chat\": {\"id\": 39327045,\"first_name\": \"\\uc11d\\uc9c4\",\"type\": \"private\"},\"date\": 1653482399,\"document\": {\"file_name\": \"test.txt\",\"mime_type\": \"text/plain\",\"file_id\": \"BQACAgUAAxkBAAIIZWKOI5-9F9n8O1nh5Bz2m505K7qfAAI5BgACNKpwVL13NImKS4jvJAQ\",\"file_unique_id\": \"AgADOQYAAjSqcFQ\",\"file_size\": 8},\"caption\": \""+caption+"\"}}]}";
    }
    public static String excelDocumentJson(String caption) {
        return "{\"ok\":true,\"result\":[{\"update_id\":567841828,\n" +
                "\"message\":{\"message_id\":2186,\"from\":{\"id\":39327045,\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1655215978,\"document\":{\"file_name\":\"sample.xlsx\",\"mime_type\":\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\",\"file_id\":\"BQACAgUAAxkBAAIIimKol2qIE4V-mkkE6t9da-u4hmuTAAIlBQACg25BVbhqN-5YQO3fJAQ\",\"file_unique_id\":\"AgADJQUAAoNuQVU\",\"file_size\":18768},\"caption\":\""+caption+"\"}}]}";
    }

    public static String photoJson(String caption) {
        return "{\"ok\": true,\"result\": [{\"update_id\": 567841808,\"message\": {\"message_id\": 2150,\"from\": {\"id\": 39327045,\"is_bot\": false,\"first_name\": \"\\uc11d\\uc9c4\",\"language_code\": \"ko\"},\"chat\": {\"id\": 39327045,\"first_name\": \"\\uc11d\\uc9c4\",\"type\": \"private\"},\"date\": 1653482413,\"photo\": [{\"file_id\": \"AgACAgUAAxkBAAIIZmKOI6wEb4PQtzRFKkLv8fPtja6tAAJYsTEbNKpwVIdigkCEtD4HAQADAgADcwADJAQ\",\"file_unique_id\": \"AQADWLExGzSqcFR4\",\"file_size\": 1196,\"width\": 90,\"height\": 90},{\"file_id\": \"AgACAgUAAxkBAAIIZmKOI6wEb4PQtzRFKkLv8fPtja6tAAJYsTEbNKpwVIdigkCEtD4HAQADAgADbQADJAQ\",\"file_unique_id\": \"AQADWLExGzSqcFRy\",\"file_size\": 15474,\"width\": 320,\"height\": 320},{\"file_id\": \"AgACAgUAAxkBAAIIZmKOI6wEb4PQtzRFKkLv8fPtja6tAAJYsTEbNKpwVIdigkCEtD4HAQADAgADeAADJAQ\",\"file_unique_id\": \"AQADWLExGzSqcFR9\",\"file_size\": 52057,\"width\": 800,\"height\": 800},{\"file_id\": \"AgACAgUAAxkBAAIIZmKOI6wEb4PQtzRFKkLv8fPtja6tAAJYsTEbNKpwVIdigkCEtD4HAQADAgADeQADJAQ\",\"file_unique_id\": \"AQADWLExGzSqcFR-\",\"file_size\": 70180,\"width\": 1280,\"height\": 1280}],\"caption\": \""+caption+"\"}}]}";
    }
}