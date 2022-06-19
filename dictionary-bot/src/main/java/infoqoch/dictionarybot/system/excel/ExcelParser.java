package infoqoch.dictionarybot.system.excel;

import infoqoch.dictionarybot.model.dictionary.DictionaryContent;

import java.util.ArrayList;
import java.util.List;

// 기존에 학습하던 영어를 어떤 엑셀의 탬플릿으로 하냐에 따라 아래의 파서가 변경됨.
// 기존에는 두 줄에 아래의 순서를 가진 형태로 정리해왔음. 이에 맞춰 작성하였으며 그 외 탬플릿 형식이나 로우 하나만을 하나의 레코드로 가지는 탬플릿을 만들 필요가 있어 보인다.
public class ExcelParser {

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