package infoqoch.dictionarybot.mock.update;

import infoqoch.dictionarybot.mock.data.MockDictionary;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.update.request.UpdateRequestMessage;
import infoqoch.dictionarybot.update.request.body.UpdateChat;
import infoqoch.dictionarybot.update.controller.resolver.param.mapper.UpdateRequestBodyParameterMapper;
import infoqoch.dictionarybot.update.controller.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

import java.util.ArrayList;
import java.util.List;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.*;

public class FakeController {
    @UpdateRequestMethodMapper(LOOKUP_SENTENCE)
    public String lookupBySentence(UpdateRequestMessage request) {
        StringBuilder sb = new StringBuilder();
        return "LOOKUP_SENTENCE : " + request.getValue();
    }

    @UpdateRequestMethodMapper(LOOKUP_DEFINITION)
    public List<Dictionary> lookupByDefinition(UpdateRequestMessage request) {
        List<Dictionary> result = new ArrayList<>();
        result.add(MockDictionary.createSimpleDictionary(MockDictionary.createSimpleDictionaryContent(), 1l));
        result.add(MockDictionary.createSimpleDictionary(MockDictionary.createSimpleDictionaryContent(), 2l));
        return result;
    }

    @UpdateRequestMethodMapper(LOOKUP_WORD)
    public UpdateResponse lookupByWord(
            UpdateRequestMessage updateRequestMessage,
            @UpdateRequestBodyParameterMapper UpdateChat chat
    ) {
        System.out.println("updateRequestMessage = " + updateRequestMessage);
        System.out.println("chat = " + chat);
        StringBuilder sb = new StringBuilder();
        sb.append(updateRequestMessage.command()).append(" : ");
        sb.append(updateRequestMessage.value()).append(" : ");
        sb.append(chat.getMessageId());

        return new UpdateResponse(SendType.MESSAGE, new MarkdownStringBuilder(sb.toString()));
    }

    @UpdateRequestMethodMapper(HELP)
    public UpdateResponse help(UpdateRequestMessage request) {
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
