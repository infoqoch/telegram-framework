package infoqoch.dictionarybot.update.dispatcher.fake;

import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.request.body.UpdateChat;
import infoqoch.dictionarybot.update.resolver.param.mapper.UpdateRequestBodyParameterMapper;
import infoqoch.dictionarybot.update.resolver.param.mapper.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.*;

public class FakeController {
    @UpdateRequestMethodMapper(LOOKUP_SENTENCE)
    public String lookupBySentence(UpdateRequest request) {
        StringBuilder sb = new StringBuilder();
        return "LOOKUP_SENTENCE : " + request.getValue();
    }

    @UpdateRequestMethodMapper(LOOKUP_DEFINITION)
    public UpdateResponse lookupByDefinition(UpdateRequest request) {
        return new UpdateResponse(SendType.MESSAGE, null);
    }

    @UpdateRequestMethodMapper(LOOKUP_WORD)
    public UpdateResponse lookupByWord(
            UpdateRequest updateRequest,
            @UpdateRequestBodyParameterMapper UpdateChat chat
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append(updateRequest.command()).append(" : ");
        sb.append(updateRequest.value()).append(" : ");
        sb.append(chat.getMessageId());

        return new UpdateResponse(SendType.MESSAGE, new MarkdownStringBuilder(sb.toString()));
    }

    @UpdateRequestMethodMapper(HELP)
    public UpdateResponse help(UpdateRequest request) {
        return new UpdateResponse(SendType.MESSAGE, new MarkdownStringBuilder("help! " + request.getValue()));
    }

    @UpdateRequestMethodMapper(UNKNOWN)
    public String unknown() {
        return "unknown??";
    }

    @UpdateRequestMethodMapper(EXCEL_PUSH)
    public String excelpush() {
        return "excep push!!";
    }
}
