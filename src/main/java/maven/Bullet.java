package maven;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Bullet {
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.lines(Path.of("output.data")).toList();
		FileWriter writer = new FileWriter("output.data");
		writer.append("\t\t\t\t\t<ul>\n");
		for (String line: lines) {
			writer.append("\t\t\t\t\t\t<li>");
			writer.append(line);
			writer.append("</li>\n");
		}
		writer.append("\t\t\t\t\t</ul>\n");
		writer.flush();
		writer.close();
	}
}
