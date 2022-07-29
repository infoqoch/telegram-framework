package infoqoch.dictionarybot.model.dictionary.service;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import infoqoch.dictionarybot.model.dictionary.DictionarySource;
import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.model.dictionary.repository.DictionarySourceRepository;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.system.excel.ExcelParser;
import infoqoch.dictionarybot.system.excel.ExcelReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class DictionaryInsertBatchService {
    private final DictionaryRepository dictionaryRepository;
    private final DictionarySourceRepository dictionarySourceRepository;

    public List<Dictionary> saveExcel(File file, DictionarySource source, ChatUser chatUser){
        final DictionarySource savedSource = dictionarySourceRepository.save(source);
        List<Dictionary> dictionaries = contentsToDictionaries(sampleExcelToContents(file), savedSource, chatUser);


        for (Dictionary dictionary : dictionaries) {
            dictionaryRepository.save(dictionary);
        }

        return dictionaries;
    }

    private List<Dictionary> contentsToDictionaries(List<List<DictionaryContent>> sheetsData, DictionarySource savedSource, ChatUser chatUser) {
        List<Dictionary> dictionaries = new ArrayList<>();
        for (List<DictionaryContent> rowsData : sheetsData) {
            for (DictionaryContent content : rowsData) {
                final Dictionary dictionary = Dictionary.builder()
                        .content(content)
                        .chatUser(chatUser)
                        .source(savedSource)
                        .build();
                dictionaries.add(dictionary);
            }
        }
        return dictionaries;
    }

    private List<List<DictionaryContent>> sampleExcelToContents(File file) {
        return ExcelParser.doubleRows(new ExcelReader(file, 4), 2);
    }
}
