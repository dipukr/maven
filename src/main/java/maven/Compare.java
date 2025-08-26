package maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Compare {
	public void compare(File file1, File file2, File file3, String sheetName, int rowStart, int rowEnd, int colStart, int colEnd) throws Exception {
		FileInputStream fileInputStream1 = new FileInputStream(file1);
		FileInputStream fileInputStream2 = new FileInputStream(file2);
		Workbook workbook1 = new HSSFWorkbook(fileInputStream1);
		Workbook workbook2 = new HSSFWorkbook(fileInputStream2);
		Sheet sheet1 = workbook1.getSheet(sheetName);
		Sheet sheet2 = workbook2.getSheet(sheetName);
		for (int row = rowStart; row <= rowEnd; row++) {
			Row row1 = sheet1.getRow(row);
			Row row2 = sheet2.getRow(row);
			for (int col = colStart; col <= colEnd; col++) {
				Cell cell1 = row1.getCell(col);
				Cell cell2 = row2.getCell(col);
				String cell1Str = cell1.toString();
				String cell2Str = cell2.toString();
				BigDecimal cell1Val = new BigDecimal(cell1Str);
				BigDecimal cell2Val = new BigDecimal(cell2Str);
				
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		String dir = "/home/dkumar/";
		File file1 = new File(dir + "dsm-reports.xlsx");
		File file2 = new File(dir + "solar-power.xlsx");
		file1.
		
	}
}
