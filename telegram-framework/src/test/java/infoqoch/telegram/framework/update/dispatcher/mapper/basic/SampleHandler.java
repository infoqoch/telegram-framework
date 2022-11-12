package infoqoch.telegram.framework.update.dispatcher.mapper.basic;

import infoqoch.telegram.framework.update.UpdateRequestMapper;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

public class SampleHandler {
    @UpdateRequestMapper({"help", "*"})
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

    @UpdateRequestMapper("voids")
    public void voids() {
        System.out.println("hi!");
    }
}
