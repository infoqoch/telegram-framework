package infoqoch.dictionarybot.util.excel;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ExcelReadTest {
    @Test
    void read_excel(){
        File file = new File(getClass().getClassLoader().getResource("exceltest/test1.xlsx").getFile());
        final ExcelReader excelReader = new ExcelReader(file, 4);
        final List<List<List<String>>> extract = excelReader.extract();
        assertThat(extract.toString()).isEqualTo("[[[a, b, c, d], [1.0, , 3.0, ], [, , , ], [, , , end]]]");
    }

}
