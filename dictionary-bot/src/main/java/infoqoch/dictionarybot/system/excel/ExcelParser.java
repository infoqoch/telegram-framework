package infoqoch.dictionarybot.system.excel;

import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

// 기존에 학습하던 영어를 어떤 엑셀의 탬플릿으로 하냐에 따라 아래의 파서가 변경됨.
// 기존에는 두 줄에 아래의 순서를 가진 형태로 정리해왔음. 이에 맞춰 작성하였으며 그 외 탬플릿 형식이나 로우 하나만을 하나의 레코드로 가지는 탬플릿을 만들 필요가 있어 보인다.
@Slf4j
public class ExcelParser {

    private static final int MAX_EMPTY_CONTENT_SIZE = 10;

    public static List<List<DictionaryContent>> doubleRows(ExcelReader excelReader, int startFrom) {
        int recordSize = 2;
        return handleSheets(excelReader, startFrom, recordSize);
    }

    private static List<List<DictionaryContent>> handleSheets(ExcelReader excelReader, int startFrom, int recordSize) {
        List<List<DictionaryContent>> sheetsData = new ArrayList<>();

        final List<List<List<String>>> sheets = excelReader.extract();
        for (List<List<String>> sheet : sheets) {
            if (isIgnoreRow(startFrom, recordSize, sheet)) continue;
            sheetsData.add(handleRows(startFrom, recordSize, sheet));
        }
        return sheetsData;
    }

    private static boolean isIgnoreRow(int startFrom, int recordSize, List<List<String>> sheet) {
        return sheet.size() < startFrom + recordSize;
    }

    private static List<DictionaryContent> handleRows(int startFrom, int recordSize, List<List<String>> sheet) {
        int emptySize = 0;
        List<DictionaryContent> rowsData = new ArrayList<>();
        for (int i = startFrom; i < sheet.size(); i += recordSize) {
            final DictionaryContent content = extractContent(sheet, i);

            if(!content.valid()){
                if(++emptySize> MAX_EMPTY_CONTENT_SIZE){
                    log.error("비어있는 열이 10개가 넘어 더 이상 현재 시트는 더는 탐색하지 않습니다.");
                    break;
                }
                continue;
            }

            rowsData.add(content);
        }
        return rowsData;
    }

    private static DictionaryContent extractContent(List<List<String>> sheet, int i) {
        final List<String> firstRow = sheet.get(i);
        final List<String> secondRow = sheet.get(i + 1);

        final DictionaryContent content = DictionaryContent.builder()
                .word(firstRow.get(1))
                .partOfSpeech(firstRow.get(2))
                .definition(firstRow.get(3))
                .pronunciation(secondRow.get(1))
                .quotation(secondRow.get(2))
                .sentence(secondRow.get(3))
                .build();
        return content;
    }
}