package infoqoch.dictionarybot.util.excel;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ExcelReadTest {
    @Test
    void read_single_sheet_excel(){
        File file = new File(getClass().getClassLoader().getResource("exceltest/test1.xlsx").getFile());
        final ExcelReader excelReader = new ExcelReader(file, 4);
        final List<List<List<String>>> extract = excelReader.extract();
        assertThat(extract.toString()).isEqualTo("[[[a, b, c, d], [1.0, , 3.0, ], [, , , ], [, , , end]]]");
    }

    @Test
    void read_double_sheet_excel(){
        File file = new File(getClass().getClassLoader().getResource("exceltest/test2.xlsx").getFile());
        final ExcelReader excelReader = new ExcelReader(file, 4);
        final List<List<List<String>>> extract = excelReader.extract();
        assertThat(extract.toString()).isEqualTo("" +
                "[" +
                "[[a, b, c, d], [1.0, , 3.0, ], [, , , ], [, , , end]]" + // 시트 1
                ", [[1.0, 2.0, 3.0, 4.0], [, , , ], [, , , ], [, 6.0, , ], [, , , 8.0]]" + // 시트 2
                "]");
    }

}
