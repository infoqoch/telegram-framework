package infoqoch.dictionarybot.model.dictionary.service;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.system.excel.ExcelReader;
import infoqoch.dictionarybot.system.excel.ExcelParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DictionaryService {
    private final DictionaryRepository dictionaryRepository;

    public int saveExcel(File file){
        // given
        List<Dictionary> dictionaries = contentsToDictionaries(sampleExcelToContents(file));

        // when
        dictionaryRepository.save(dictionaries);

        return dictionaries.size();
    }

    private List<Dictionary> contentsToDictionaries(List<List<DictionaryContent>> sheetsData) {
        List<Dictionary> dictionaries = new ArrayList<>();
        final String sourceId = UUID.randomUUID().toString();
        for (List<DictionaryContent> rowsData : sheetsData) {
            for (DictionaryContent content : rowsData) {
                final Dictionary dictionary = Dictionary.builder()
                        .content(content)
                        .source(Dictionary.Source.EXCEL)
                        .sourceId(sourceId)
                        .build();
                dictionaries.add(dictionary);
            }
        }
        return dictionaries;
    }

    private List<List<DictionaryContent>> sampleExcelToContents(File file) {
        return ExcelParser.doubleRows(new ExcelReader(file, 4));
    }
}
