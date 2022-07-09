package infoqoch.dictionarybot.model.dictionary.service;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import infoqoch.dictionarybot.model.dictionary.repository.DictionaryRepository;
import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.system.excel.ExcelReader;
import infoqoch.dictionarybot.system.excel.ExcelParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class DictionaryInsertBatchService {
    private final DictionaryRepository dictionaryRepository;

    public int saveExcel(File file, ChatUser chatUser){
        // given
        List<Dictionary> dictionaries = contentsToDictionaries(sampleExcelToContents(file), chatUser);

        // when
        for (Dictionary dictionary : dictionaries) {
            dictionaryRepository.save(dictionary);
        }

        return dictionaries.size();
    }

    private List<Dictionary> contentsToDictionaries(List<List<DictionaryContent>> sheetsData, ChatUser chatUser) {
        List<Dictionary> dictionaries = new ArrayList<>();
        final String sourceId = UUID.randomUUID().toString();
        for (List<DictionaryContent> rowsData : sheetsData) {
            for (DictionaryContent content : rowsData) {
                final Dictionary dictionary = Dictionary.builder()
                        .content(content)
                        .chatUser(chatUser)
                        .insertType(Dictionary.InsertType.EXCEL)
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
