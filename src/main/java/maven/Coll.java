package maven;

import java.io.File;
import java.nio.file.Files;

public class Coll {
	public static String wrap(String line) {
		var data = new StringBuilder();
		data.append("<li>");
		data.append(line.trim());
		data.append("</li>");
		return data.toString();
	}
	public static int tabCount(String line) {
		int count = 0;
		for (int i = 0; i < line.length() && line.charAt(i) == '\t'; i++)
			count += 1;
		return count;
	}
	public static void main(String[] args) throws Exception {
		var data = new StringBuilder();
		File file = new File("/home/dkumar/collection/untitled2");
		int prevCount = 0;
		int inBlock = 0;
		for (String line: Files.lines(file.toPath()).toList()) {
			if (line.isEmpty()) {
				data.append("<br><br>");
				continue;
			}
			int tabCount = tabCount(line);
			if (tabCount == 0 && inBlock == 0) {
				data.append(line);
				continue;
			}
			if (tabCount == prevCount && inBlock > 0) {
				data.append(wrap(line));
				continue;
			}
			if (tabCount < prevCount && inBlock > 0) {
				data.append("</ul>");
				data.append(wrap(line));
				inBlock -= 1;
			}
			if (tabCount < prevCount && inBlock == 0) {
				data.append(line);
			}
			if (tabCount > prevCount) {
				inBlock += 1;
				data.append("<ul>");
				data.append(wrap(line));
			}
			prevCount = tabCount;
		}
		System.out.println(data);
	}
}












