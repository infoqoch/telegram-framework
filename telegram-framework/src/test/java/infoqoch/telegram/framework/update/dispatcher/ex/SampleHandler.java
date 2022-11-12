package infoqoch.telegram.framework.update.dispatcher.ex;

import infoqoch.telegram.framework.update.UpdateRequestMapper;
import infoqoch.telegram.framework.update.exception.TelegramClientException;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

public class SampleHandler {
    @UpdateRequestMapper("help")
    public String help(){
        return "I am going to help you!";
    }

    @UpdateRequestMapper("hello")
    public String hello(){
        return "hi!";
    }

    @UpdateRequestMapper("docu")
    public UpdateResponse document() {
        return UpdateResponse.document(new MarkdownStringBuilder("send document!"), "document_as_file_id");
    }

    @UpdateRequestMapper("*")
    public String any(){
        throw new TelegramClientException(new MarkdownStringBuilder("정확한 명령어를 입력해야 합니다."), "명확하지 않는 명령어");
    }

    @UpdateRequestMapper("noresponse")
    public String noresponseException(){
        throw new IllegalArgumentException();
    }

}
