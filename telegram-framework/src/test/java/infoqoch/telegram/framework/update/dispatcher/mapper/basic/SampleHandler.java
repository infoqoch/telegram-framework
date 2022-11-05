package infoqoch.telegram.framework.update.dispatcher.mapper.basic;

import infoqoch.telegram.framework.update.UpdateRequestMethodMapper;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

public class SampleHandler {
    @UpdateRequestMethodMapper({"help", "*"})
    public String help(){
        return "I am going to help you!";
    }

    @UpdateRequestMethodMapper("hello")
    public String hello(){
        return "hi!";
    }

    @UpdateRequestMethodMapper("docu")
    public UpdateResponse document() {
        return UpdateResponse.document(new MarkdownStringBuilder("send document!"), "document_as_file_id");
    }

}
