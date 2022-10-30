package infoqoch.dictionarybot.system.excel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// poi의 경우 누락된 셀에 대해서는 index에도 포함하지 않고 넘어가버린다.
// 하지만 특정 칼럼이 누락된 데이터는 언제나 존재한다. 이를 방지하기 위하여 모든 행열에 대하여 index 기준으로 저장하고 탐색한다.
public class ExcelReaderTest {
    @DisplayName("ExcelReader의 동작을 확인한다.")
    @Test
    void read_single_sheet_excel(){
        File file = new File(getClass().getClassLoader().getResource("exceltest/test1.xlsx").getFile());
        final ExcelReader excelReader = new ExcelReader(file, 4);
        final List<List<List<String>>> extract = excelReader.extract();
        assertThat(extract.toString()).isEqualTo("[[[a, b, c, d], [1.0, null, 3.0, null], [null, null, null, null], [null, null, null, end]]]");
        for (List<List<String>> lists : extract) {
            System.out.println("lists = " + lists);
        }
    }

    @DisplayName("Sheet이 여러개인 엑셀에 대한 ExcelReader의 동작을 확인한다.")
    @Test
    void read_double_sheet_excel(){
        File file = new File(getClass().getClassLoader().getResource("exceltest/test2.xlsx").getFile());
        final ExcelReader excelReader = new ExcelReader(file, 4);
        final List<List<List<String>>> extract = excelReader.extract();
        assertThat(extract.toString()).isEqualTo("" +
                "[" +
                "[[a, b, c, d], [1.0, null, 3.0, null], [null, null, null, null], [null, null, null, end]]" + // 시트 1
                ", [[1.0, 2.0, 3.0, 4.0], [null, null, null, null], [null, null, null, null], [null, 6.0, null, null], [null, null, null, 8.0]]" + // 시트 2
                "]");
    }
}
