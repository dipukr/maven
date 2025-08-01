package maven;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Main {
	public static void main(final String[] args) throws Exception {
		List<String> lines = 
				Files.lines(Path.of("/home/dkumar/RESEARCH/data"))
				.toList();
		var text = new StringBuilder();
		int lineNo = 0;
		while (lineNo < lines.size()) {			
			if (lines.get(lineNo).endsWith(":")) {
				text.append(String.format("<br><b>%s</b>", lines.get(lineNo)));
				lineNo++;
				text.append("<ul>");
				while (lineNo < lines.size()) {
					if (lines.get(lineNo).trim().isBlank()) break;
					
					text.append(String.format("<li>%s</li>", lines.get(lineNo)));
					lineNo++;
				}
				text.append("</ul>");
			} else {
				text.append(lines.get(lineNo));
			}
			lineNo++;
		}
		System.out.println(text);
		var writer = new PrintWriter("/home/dkumar/RESEARCH/output");
		//writer.write(text.toString());
		writer.flush();
		writer.close();
		
	}
}
