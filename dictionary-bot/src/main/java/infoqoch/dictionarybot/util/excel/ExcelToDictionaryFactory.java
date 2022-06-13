package infoqoch.dictionarybot.util.excel;

import infoqoch.dictionarybot.model.dictionary.DictionaryContent;

import java.util.ArrayList;
import java.util.List;

public class ExcelToDictionaryFactory {
    public static List<List<DictionaryContent>> doubleRows(ExcelReader excelReader) {
        int startFrom = 2;
        int recordSize = 2;

        List<List<DictionaryContent>> sheetsData = new ArrayList<>();

        final List<List<List<String>>> sheets = excelReader.extract();
        for (List<List<String>> sheet : sheets) {
            List<DictionaryContent> rowsData = new ArrayList<>();

            if (sheet.size() < startFrom + recordSize) continue;
            for (int i = startFrom; i < sheet.size(); i += recordSize) {
                final List<String> firstRow = sheet.get(i);
                final List<String> secondRow = sheet.get(i + 1);

                final DictionaryContent content = DictionaryContent.builder()
                        .word(firstRow.get(1))
                        .partOfSpeech(firstRow.get(2))
                        .definition(firstRow.get(3))
                        .pronunciation(secondRow.get(1))
                        .source(secondRow.get(2))
                        .sentence(secondRow.get(3))
                        .build();

                rowsData.add(content);
            }
            sheetsData.add(rowsData);
        }
        return sheetsData;
    }
}