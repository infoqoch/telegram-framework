package infoqoch.dictionarybot.system.excel;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ExcelReaderTest {
    @Test
    void read_single_sheet_excel(){
        File file = new File(getClass().getClassLoader().getResource("exceltest/test1.xlsx").getFile());
        final ExcelReader excelReader = new ExcelReader(file, 4);
        final List<List<List<String>>> extract = excelReader.extract();
        assertThat(extract.toString()).isEqualTo("[[[a, b, c, d], [1.0, null, 3.0, null], [null, null, null, null], [null, null, null, end]]]");
    }

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
