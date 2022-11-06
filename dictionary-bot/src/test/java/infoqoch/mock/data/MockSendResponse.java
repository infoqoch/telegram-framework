package infoqoch.mock.data;

public class MockSendResponse {

    public static String sendMessage(String text, long chatId) {
        return "{\"ok\":true,\"result\":{\"message_id\":2092,\"from\":{\"id\":1959903402,\"is_bot\":true,\"first_name\":\"coffs_test\",\"username\":\"coffs_dic_test_bot\"},\"chat\":{\"id\":" + chatId + ",\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652014357,\"text\":\"" + text + "\"}}";
    }

    public static String sendDocument(long chatId) {
        return "{\"ok\":true,\"result\":{\"message_id\":2143,\"from\":{\"id\":1959903402,\"is_bot\":true,\"first_name\":\"coffs_test\",\"username\":\"coffs_dic_test_bot\"},\"chat\":{\"id\":"+chatId+",\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652609308,\"document\":{\"file_name\":\"sample.xlsx\",\"mime_type\":\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\",\"file_id\":\"BQACAgUAAxkDAAIIX2KA0RyYEZNXxw7qiny1i0Jj7-RqAAL_BAACg56JVdF3guuN7A6tJAQ\",\"file_unique_id\":\"AgAD_wQAAoOeiVU\",\"file_size\":26440},\"caption\":\"\\uc774\\ud0c8\\ub9ad\\uba54\\uc2dc\\uc9c0!\",\"caption_entities\":[{\"offset\":0,\"length\":7,\"type\":\"italic\"}]}}";
    }
}