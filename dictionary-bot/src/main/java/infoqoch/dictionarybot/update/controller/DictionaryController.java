package infoqoch.dictionarybot.update.controller;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryRepository;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.request.body.UpdateChat;
import infoqoch.dictionarybot.update.resolver.mapper.UpdateRequestBodyParameterMapper;
import infoqoch.dictionarybot.update.resolver.mapper.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.*;

@Controller
@AllArgsConstructor
public class DictionaryController {
    private final DictionaryRepository dictionaryRepository;

    @UpdateRequestMethodMapper(LOOKUP_WORD)
    public UpdateResponse lookupByWord(UpdateRequest updateRequest) {
        System.out.println("UpdateRequestMethodMapper : lookupByWord!");
        final List<Dictionary> result = dictionaryRepository.findByWord(updateRequest.getValue());
        return new UpdateResponse(SendType.MESSAGE, result.toString());
    }

    @UpdateRequestMethodMapper(HELP)
    public String help() {
        System.out.println("UpdateRequestMethodMapper : help!");
        return "요래 저래 쓰면 된단다!";
    }

//    TODO!
//    @UpdateRequestMethodMapper(LOOKUP_SENTENCE)
//    public String lookupBySentence(UpdateRequest request) {
//        StringBuilder sb = new StringBuilder();
//        return "LOOKUP_SENTENCE : " + request.getValue();
//    }
}
