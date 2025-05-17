package maven;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class RM {
	
	public static final String RESET = "\u001B[0m";
	public static final String RED = "\u001B[31m";
	
	public static void main(final String[] args) {
		try {
			List<File> files = Files.lines(Path.of(".rmall"))
				.map(File::new)
				.filter(File::isFile)
				.filter(File::exists)
				.toList();
			for (File file: files)
				System.out.printf("%sFile:%s %s\n", RED, RESET, file.getAbsolutePath());
			System.out.print("Proceed? [Yes/No]: ");
			try (Scanner scanner = new Scanner(System.in)) {
				String word = scanner.next();
				if (!word.equalsIgnoreCase("Yes"))
					return;
			}
			for (File file: files) {
				if (file.delete()) {
					System.out.printf("%sDeleted:%s %s\n", RED, RESET, file.getAbsolutePath());
				} else {
					System.out.printf("%sFailed:%s %s\n", RED, RESET, file.getAbsolutePath());
				}
			}
		} catch (IOException e) {
			System.getenv("HOME");
			System.out.println("ERROR: file '$HOME/.rmall' not found.");
		}
	}
}