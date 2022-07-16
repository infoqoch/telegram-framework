package infoqoch.dictionarybot.mock.update;

import infoqoch.dictionarybot.mock.data.MockDictionary;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.update.exception.TelegramClientException;
import infoqoch.dictionarybot.update.request.UpdateRequestMessage;
import infoqoch.dictionarybot.update.request.body.UpdateChat;
import infoqoch.dictionarybot.update.controller.resolver.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.send.SendType;
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
            UpdateChat chat
    ) {
        System.out.println("updateRequestMessage = " + updateRequestMessage);
        System.out.println("chat = " + chat);
        StringBuilder sb = new StringBuilder();
        sb.append(updateRequestMessage.getCommand()).append(" : ");
        sb.append(updateRequestMessage.getValue()).append(" : ");
        sb.append(chat.getMessageId());

        return new UpdateResponse(SendType.MESSAGE, new MarkdownStringBuilder(sb.toString()));
    }

    @UpdateRequestMethodMapper(HELP)
    public UpdateResponse help(UpdateRequestMessage request) {
        if(request.getValue().contains("exception"))
            throw new TelegramClientException(new MarkdownStringBuilder("잘못된 값을 입력하였습니다! 확인 바랍니다."), "사용자가 잘못된 데이터를 입력하였습니다.");
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

    @UpdateRequestMethodMapper(SHARE_MINE)
    public String shareMine() {
        return "unknown??";
    }


    @UpdateRequestMethodMapper(LOOKUP_ALL_USERS)
    public String lookupAllUsers() {
        return "unknown??";
    }

    @UpdateRequestMethodMapper(PROMOTION_ROLE)
    public String promotion() {
        return "unknown??";
    }

    @UpdateRequestMethodMapper(HOURLY_ALARM)
    public String hourly() {
        return "unknown??";
    }
    @UpdateRequestMethodMapper(MY_STATUS)
    public String status() {
        return "unknown??";
    }

}
