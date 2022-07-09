package infoqoch.dictionarybot.model.dictionary.repository;

import infoqoch.dictionarybot.mock.repository.MemoryDictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import infoqoch.dictionarybot.system.excel.ExcelReader;
import infoqoch.dictionarybot.system.excel.ExcelParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Primary
@Profile("!test_jpa")
@Repository
public class MemoryDictionaryRepositoryTest {
    DictionaryRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MemoryDictionaryRepository();
    }

    @Test
    void save(){
        final Long dictionaryNo = saveInRepo(createSimpleDictionaryContent("apple"));
        assertThat(dictionaryNo).isGreaterThan(0);
    }

    @Test
    void find_by_no(){
        // given
        final DictionaryContent content = createSimpleDictionaryContent("first");
        final Long dictionaryNo = saveInRepo(content);

        // when
        Optional<Dictionary> result = repository.findByNo(dictionaryNo);

        // then
        Assertions.assertThat(result).isPresent();
        assertThat(dictionaryNo).isEqualTo(1l);
        assertThat(result.get().getNo()).isEqualTo(dictionaryNo);
        assertThat(result.get().getContent()).usingRecursiveComparison().isEqualTo(content);
    }

    @Test
    void increment_pk(){
        // given
        final Long firstNo = saveInRepo(createSimpleDictionaryContent("first"));
        assert firstNo == 1l;

        final DictionaryContent content = createSimpleDictionaryContent("second");
        final Long dictionaryNo = saveInRepo(content);

        // when
        Optional<Dictionary> result = repository.findByNo(dictionaryNo);

        // then
        Assertions.assertThat(result).isPresent();
        assertThat(dictionaryNo).isEqualTo(2l);
        assertThat(result.get().getNo()).isEqualTo(dictionaryNo);
        assertThat(result.get().getContent()).usingRecursiveComparison().isEqualTo(content);
    }

    @Test
    void find_by_word(){
        // given
        saveInRepo(createSimpleDictionaryContent("kimchi")); // 허수
        DictionaryContent content = createSimpleDictionaryContent("apple");
        final Long dictionaryNo = saveInRepo(content);
        saveInRepo(createSimpleDictionaryContent("radio")); // 허수

        // when
        List<Dictionary> result = repository.findByContentWord("apple");

        // then
        Assertions.assertThat(result).size().isEqualTo(1);
        assertThat(result.get(0).getNo()).isEqualTo(dictionaryNo);
        assertThat(result.get(0).getContent()).usingRecursiveComparison().isEqualTo(content);
    }


    @Test
    void find_by_word_not_found(){
        // given
        saveInRepo(createSimpleDictionaryContent("kimchi")); // 허수

        // when
        List<Dictionary> result = repository.findByContentWord("apple");

        // then
        Assertions.assertThat(result).size().isEqualTo(0);
    }


    private Long saveInRepo(DictionaryContent dictionaryContent) {
        final Dictionary dictionary = Dictionary.builder().content(dictionaryContent).build();
        return repository.save(dictionary).getNo();
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

    @Test
    void save_list(){
        // given
        List<Dictionary> dictionaries = contentsToDictionaries(sampleExcelToContents());

        // when
        for (Dictionary dictionary : dictionaries) {
            repository.save(dictionary);
        }


        // then
        final List<Dictionary> all = repository.findAll();
        assertThat(all).size().isEqualTo(dictionaries.size());
        assertThat(toWordSet(all)).containsAll(toWordSet(dictionaries));
    }

    private List<Dictionary> contentsToDictionaries(List<List<DictionaryContent>> sheetsData) {
        List<Dictionary> dictionaries = new ArrayList<>();
        final String sourceId = UUID.randomUUID().toString();
        for (List<DictionaryContent> rowsData : sheetsData) {
            for (DictionaryContent content : rowsData) {
                final Dictionary dictionary = Dictionary.builder()
                        .content(content)
                        .insertType(Dictionary.InsertType.EXCEL)
                        .build();
                dictionaries.add(dictionary);
            }
        }
        return dictionaries;
    }

    private List<List<DictionaryContent>> sampleExcelToContents() {
        File file = new File(getClass().getClassLoader().getResource("exceltest/sample.xlsx").getFile());
        final ExcelReader excelReader = new ExcelReader(file, 4);
        List<List<DictionaryContent>> sheetsData = ExcelParser.doubleRows(excelReader);
        return sheetsData;
    }

    private Set<String> toWordSet(List<Dictionary> all) {
        return all.stream().map(d -> d.getContent().getWord()).collect(Collectors.toSet());
    }

}