package maven;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class Excels {
	public static void createCell(Sheet sheet, int rowNo, int cellNo, String cellValue) {
		Row row = sheet.getRow(rowNo);
		if (row == null) row = sheet.createRow(rowNo);
		Cell cell = row.createCell(cellNo);
		cell.setCellValue(cellValue);
	}
}
