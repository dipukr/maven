package maven;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {
	public static void writeAll(Sheet sheet, String text) {
		Excels.createCell(sheet, 0, 0, text);
	}
	public static void main(final String[] args) throws Exception {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("text");
		File file = new File("text.xlsx");
		List<Thread> threads = new ArrayList<>();
		for (int i = 1; i <= 4; i++) {
			final int a = i;
			Thread thread = new Thread(() -> writeAll(workbook.createSheet("sheet"+a), "hello"));
			threads.add(thread);
			thread.start();
		}
		for (Thread thread: threads) thread.join();
		FileOutputStream fos = new FileOutputStream(file);
		try {
			workbook.write(fos);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
