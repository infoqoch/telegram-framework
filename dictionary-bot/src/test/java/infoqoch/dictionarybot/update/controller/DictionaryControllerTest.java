package infoqoch.dictionarybot.update.controller;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.repository.MemoryDictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.service.DictionaryService;
import org.junit.jupiter.api.Test;

class DictionaryControllerTest {
    DictionaryRepository repository = new MemoryDictionaryRepository();
    DictionaryService service = new DictionaryService(repository);
    DictionaryController controller = new DictionaryController(repository, service);

    // TODO
    @Test
    void help(){
//        saveInRepo(createSimpleDictionaryContent("apple"));
//        final UpdateResponse response = controller.lookupByWord(new UpdateRequest(LOOKUP_WORD, "apple"));
//        System.out.println("response.body() = " + response.message());
//        assertThat(response.message()).isInstanceOf(String.class);
//        final String body = (String) response.message();
//        System.out.println("body = " + body);
    }

    private Long saveInRepo(DictionaryContent dictionaryContent) {
        final Dictionary dictionary = Dictionary.builder().content(dictionaryContent).build();
        return repository.save(dictionary);
    }

    private DictionaryContent createSimpleDictionaryContent(String word) {
        DictionaryContent content = DictionaryContent.builder()
                .word(word)
                .pronunciation("애포얼")
                .partOfSpeech("noun")
                .source("아낌없이 주는 나무")
                .definition("사과")
                .sentence("Iphone 7 is the latest model")
                .build();
        return content;
    }
}