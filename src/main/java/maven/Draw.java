package maven;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Draw {
	public static void main(String[] args) {
		if (args.length == 1) {
			try {
				Files.lines(Path.of(args[0]))
					.map(line -> line.replaceAll("\t", "    "))
					.forEach(System.out::println);
			} catch (FileNotFoundException e) {
				System.out.printf("File '%s' not found.", args[0]);
			} catch (IOException e) {
				System.out.println("File IO error.");
			}
		}
	}
}
