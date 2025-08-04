package maven;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Utils {
	public void htmlUtils(String fileName) throws Exception {
		List<String> lines = 
				Files.lines(Path.of(fileName))
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
	}
}
