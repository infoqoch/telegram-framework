package infoqoch.dictionarybot.system.excel;

import infoqoch.dictionarybot.model.dictionary.DictionaryContent;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExcelParserTest {


    @Test
    void double_row(){
        File file = new File(getClass().getClassLoader().getResource("exceltest/sample.xlsx").getFile());
        final ExcelReader excelReader = new ExcelReader(file, 4);

        List<List<DictionaryContent>> sheetsData = ExcelParser.doubleRows(excelReader);

        assertThat(sheetsData.get(0)).size().isEqualTo(9);
        assertThat(sheetsData.get(1)).size().isEqualTo(19);
        assertThat(sheetsData.get(2)).size().isEqualTo(19);

        assertThat(sheetsData.get(0).get(0).getWord()).isEqualTo("hang up");
        assertThat(sheetsData.get(0).get(0).getPartOfSpeech()).isEqualTo("sen.");
        assertThat(sheetsData.get(0).get(0).getDefinition()).isEqualTo("He hung up on me ; 그가 화가나서 내 전화를 끊었다는 의미");
        assertThat(sheetsData.get(0).get(0).getPronunciation()).isEqualTo(null);
        assertThat(sheetsData.get(0).get(0).getSource()).isEqualTo("SO book7");
        assertThat(sheetsData.get(0).get(0).getSentence()).isEqualTo(null);

    }
}
