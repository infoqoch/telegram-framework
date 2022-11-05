package infoqoch.dictionarybot.mock.update;

import infoqoch.dictionarybot.mock.data.MockDictionary;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.telegram.framework.update.UpdateRequestMethodMapper;
import infoqoch.telegram.framework.update.exception.TelegramClientException;
import infoqoch.telegram.framework.update.request.UpdateRequestCommandAndValue;
import infoqoch.telegram.framework.update.request.body.UpdateChat;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class FakeController {
    @UpdateRequestMethodMapper("s")
    public String lookupBySentence(UpdateRequestCommandAndValue request) {
        StringBuilder sb = new StringBuilder();
        return "LOOKUP_SENTENCE : " + request.getValue();
    }

    @UpdateRequestMethodMapper("d")
    public List<Dictionary> lookupByDefinition(UpdateRequestCommandAndValue request) {
        List<Dictionary> result = new ArrayList<>();
        result.add(MockDictionary.createSimpleDictionary(MockDictionary.createSimpleDictionaryContent(), 1l));
        result.add(MockDictionary.createSimpleDictionary(MockDictionary.createSimpleDictionaryContent(), 2l));
        return result;
    }

    @UpdateRequestMethodMapper("w")
    public UpdateResponse lookupByWord(
            UpdateRequestCommandAndValue updateRequestCommandAndValue,
            UpdateChat chat
    ) {
        System.out.println("updateRequestMessage = " + updateRequestCommandAndValue);
        System.out.println("chat = " + chat);
        StringBuilder sb = new StringBuilder();
        sb.append(updateRequestCommandAndValue.getCommand().get()).append(" : ");
        sb.append(updateRequestCommandAndValue.getValue()).append(" : ");
        sb.append(chat.getMessageId());

        return UpdateResponse.message(new MarkdownStringBuilder(sb.toString()));
    }

    @UpdateRequestMethodMapper({"help", "help excel"})
    public UpdateResponse help(UpdateRequestCommandAndValue request) {
        if(request.getValue().contains("exception"))
            throw new TelegramClientException(new MarkdownStringBuilder("잘못된 값을 입력하였습니다! 확인 바랍니다."), "사용자가 잘못된 데이터를 입력하였습니다.");
        return UpdateResponse.message(new MarkdownStringBuilder("help! " + request.getValue()));
    }

    @UpdateRequestMethodMapper("*")
    public String unknown() {
        return "unknown??";
    }

    @UpdateRequestMethodMapper("excel push")
    public String excelpush() {
        return "excep push!!";
    }

    @UpdateRequestMethodMapper("share mine")
    public String shareMine(RuntimeException runtimeException) {
        return "unknown??";
    }


    @UpdateRequestMethodMapper("lookup all users")
    public String lookupAllUsers() {
        return "unknown??";
    }

    @UpdateRequestMethodMapper("promotion")
    public String promotion() {
        return "unknown??";
    }

    @UpdateRequestMethodMapper("hourly")
    public String hourly() {
        return "unknown??";
    }

    @UpdateRequestMethodMapper({"status", "f"})
    public String multipleCommands() {
        return "multipleCommands called";
    }

}
