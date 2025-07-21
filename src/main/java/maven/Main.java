package maven;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Main {
	public static void main(final String[] args) throws Exception {
		Path path = Path.of("/home/dkumar/RESEARCH/words");
		Set<String> words = new TreeSet<>();
		List<String> lines = Files.lines(path).toList();
		for (String line: lines) {
			boolean valid = true;
			if (line.length() < 3) continue;
			for (char ch: line.toCharArray()) {
				if (!Character.isLetter(ch)) {
					valid = false;
					break;
				}
			}
			if (valid) words.add(line);
		}
		FileWriter writer = new FileWriter(path.toFile());
		for (String word: words)
			writer.write(word+' ');
		writer.flush();
		writer.close();
	}
}
