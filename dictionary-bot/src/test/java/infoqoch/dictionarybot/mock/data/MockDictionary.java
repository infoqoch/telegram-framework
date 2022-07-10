package infoqoch.dictionarybot.mock.data;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;

public class MockDictionary {
    public static Dictionary createSimpleDictionary(DictionaryContent content, long no) {
        return Dictionary.builder().no(no).content(content).build();
    }

    public static DictionaryContent createSimpleDictionaryContent() {
        DictionaryContent content = DictionaryContent.builder()
                .word("apple")
                .pronunciation("애포얼")
                .partOfSpeech("noun")
                .quotation("아낌없이 주는 나무")
                .definition("사과")
                .sentence("Iphone 7 is the latest model")
                .build();
        return content;
    }
}