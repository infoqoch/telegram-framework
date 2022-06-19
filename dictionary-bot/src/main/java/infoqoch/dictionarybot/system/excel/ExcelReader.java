package infoqoch.dictionarybot.system.excel;

import infoqoch.dictionarybot.system.exception.TelegramServerException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
* poi의 사용이 다소 까다롭기 때문에 불가피하게 주석을 작성함.
*
* 1. iterator를 사용하면 안됨. 공란인 데이터가 누락됨.
* 	- 셀 : [ab] [bb] [cc] [] [ee] 라는 row가 있을 경우, iterator를 사용할 경우셀의 개수를 4개를 반환함.
* 	- 로우 : 아무것도 없는 로우는 그냥 누락함
* 	- 개인적으로 poi를 이렇게 만든 것이 잘 이해가지 않음. 공란도 분명하게 데이터인데 왜 마음대로 누락하는지...
* 	- poi를 컴퍼지션으로 구현한 라이브러리가 존재하나 iterator를 기본으로 사용하여 쓸 수 없었음.
*
* 2. cell의 최대 개수를 추출하기 어려움.
* 	- iterator를 우회하여 cell의 index로 값을 추출할 수 있음. 그러니까 [ab] [bb] [cc] [] [ee] 가 있을 때, getCell(3)은 empty를, getCell(4)는 ee를 출력함.
*   - 해당 row의 개수를 반환할 경우 4만 반환하므로 강제로 5로 맞춰야 하는 문제가 있음.
*   - 아래는 모든 row를 탐색하여 최대 길이를 추출하고 이를 cell의 사이즈로 구현하였음. (numberOfCell). 다만 기대한 값은 5이지만 위와 같이 모든 로우에서 셀이 5개가 아닌 경우가 있다면 4를 리턴하게 됨. 완전하지 않음.
*
* 3. 결론적으로...
* - 가능하면 엑셀 그대로를 담기 위하여 노력하였음.
* 	- cell의 최대 개수는 생성자에서 명시하도록 함.
* 	- row가 공란이라 하더라도 empty string으로 이뤄진 리스트를 넣음.
* - 사용자로 하여금 엑셀을 엄격하게 작성하도록 해야 할 듯.
*
*/

public class ExcelReader {
	private final Workbook workbook;
	private Integer numberOfCell;

	public ExcelReader(File file, Integer numberOfCell) {
		this(defaultWorkbook(file), numberOfCell);
	}

	public ExcelReader(Workbook workbook, Integer numberOfCell) {
		this.workbook = workbook;
		this.numberOfCell = numberOfCell;
	}

	private static Workbook defaultWorkbook(File file) {
		try (Workbook workbook = new XSSFWorkbook(new FileInputStream(file))) {
			return workbook;
		} catch (IOException e) {
			throw new TelegramServerException(e);
		}
	}

	public List<List<List<String>>> extract() {
		// if(numberOfCell==null) numberOfCell = findMaxNumberOfCells(workbook);
        return extractSheets(workbook);
	}

	private List<List<List<String>>> extractSheets(Workbook workbook) {
		List<List<List<String>>> sheesData = new ArrayList<>();
        for(int h=0; h<workbook.getNumberOfSheets(); h++) {
        	Sheet sheet = workbook.getSheetAt(h);
        	List<List<String>> rowsData = extractRows(sheet);
            sheesData.add(rowsData);
        }
        return sheesData;
	}

	private List<List<String>> extractRows(Sheet sheet) {
		int size = sheet.getPhysicalNumberOfRows();
		List<List<String>> rowsData = new ArrayList<>();

		int i = -1;
		while(++i < size) {
			Row row = sheet.getRow(i);

			if(row!=null) {
		    	rowsData.add(extractCells(row));
			} else {
				rowsData.add(emptyList());
				size++;
			}
		}
		return rowsData;
	}

	private List<String> emptyList() {
		List<String> cellsData = new ArrayList<>();
		for(int j=0; j<numberOfCell; j++) {
			cellsData.add(null);
		}
		return cellsData;
	}

	private List<String> extractCells(Row row) {
		List<String> cellsData = new ArrayList<>();
		for(int j=0; j<numberOfCell; j++) {
			cellsData.add(cellToString(row.getCell(j)));
		}
		return cellsData;
	}

	private String cellToString(Cell cell) {
		if(cell==null) return null;

		switch (cell.getCellType()) {
	        case STRING: return cell.getStringCellValue();
	        case NUMERIC: return String.valueOf(cell.getNumericCellValue());
	        case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
	        case FORMULA: return String.valueOf(cell.getCellFormula() );
	        default: return null;
		}
	}

	private ExcelReader(File file) {
		this(file, null);
	}

	private ExcelReader(Workbook workbook) {
		this(workbook, null);
	}

	private Integer findMaxNumberOfCells(Workbook workbook) {
		int max = 0;

		for(Sheet sheet : workbook)
			for(Row row : sheet)
				if(row.getPhysicalNumberOfCells() > max) max = row.getPhysicalNumberOfCells();
		return max;
	}
}
