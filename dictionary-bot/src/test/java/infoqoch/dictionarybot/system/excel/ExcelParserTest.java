package infoqoch.dictionarybot.system.excel;

import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// TODO
// 읽을 수 있는 엑셀 타입이 한정됨. 몇 가지 패턴과 샘플이 필요.
public class ExcelParserTest {
    @DisplayName("ExcelParser의 동작여부를 확인. dictionary를 엑셀로 저장할 때 사용하는 엑셀 탬플릿을 테스트한다.")
    @Test
    void double_row(){
        File file = new File(getClass().getClassLoader().getResource("exceltest/dictionary_test.xlsx").getFile());
        final ExcelReader excelReader = new ExcelReader(file, 4);

        List<List<DictionaryContent>> sheetsData = ExcelParser.doubleRows(excelReader, 2);

        assertThat(sheetsData.get(0)).size().isEqualTo(9);
        assertThat(sheetsData.get(1)).size().isEqualTo(19);
        assertThat(sheetsData.get(2)).size().isEqualTo(19);

        assertThat(sheetsData.get(0).get(0).getWord()).isEqualTo("hang up");
        assertThat(sheetsData.get(0).get(0).getPartOfSpeech()).isEqualTo("sen.");
        assertThat(sheetsData.get(0).get(0).getDefinition()).isEqualTo("He hung up on me ; 그가 화가나서 내 전화를 끊었다는 의미");
        assertThat(sheetsData.get(0).get(0).getPronunciation()).isEqualTo(null);
        assertThat(sheetsData.get(0).get(0).getQuotation()).isEqualTo("SO book7");
        assertThat(sheetsData.get(0).get(0).getSentence()).isEqualTo(null);
    }


    @DisplayName("무한 loop로 끝나지 않는 엑셀을 다룬다.")
    @Test
    void double_row_infinitive(){
        File file = new File(getClass().getClassLoader().getResource("exceltest/infinitive_excel.xlsx").getFile());
        final ExcelReader excelReader = new ExcelReader(file, 4);
        List<List<DictionaryContent>> sheetsData = ExcelParser.doubleRows(excelReader, 2);

        assertThat(sheetsData.get(0)).size().isEqualTo(2);
    }


}
