package maven;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class OPUtils {
	public void genCPP(List<String> lines) {
		File file = new File("~/DSA/NeemVM/opcode.h");
		try (var writer = new PrintWriter(file)) {
			writer.write("#ifndef NEEM_OPCODE_H\n");
			writer.write("#define NEEM_OPCODE_H\n\n");
			writer.write("namespace neem {\n\n");
			writer.write("enum opcode {\n");
			var iter = lines.iterator();
			int counter = 0;
			while (iter.hasNext()) {
				writer.printf("\t%s", iter.next(), counter++);
				if (iter.hasNext())
					writer.printf(",\n");
			}
			writer.write("\n}\n\n}");
			writer.write("#endif");
		} catch (FileNotFoundException e) {
			System.out.printf("ERROR: file '%s' not found.", file.getName());
			System.exit(0);
		}
	}

	public void genJava(List<String> lines) {
		File file = new File("~/DSA/neem/src/main/java/neem/Opcode.java");
		try (var writer = new PrintWriter(file)) {
			writer.write("package neem;\n\n");
			writer.write("public class Opcode {\n");
			var iter = lines.iterator();
			int counter = 0;
			while (iter.hasNext()) {
				writer.printf("\tpublic static final byte %s = 0x%x;", iter.next(), counter++);
				if (iter.hasNext())
					writer.printf("\n");
			}
			writer.write("\n}");
		} catch (FileNotFoundException e) {
			System.out.printf("ERROR: file '%s' not found.", file.getName());
			System.exit(0);
		}
	}
	
	public List<String> getLines(String filePath) {
		var lines = new ArrayList<String>();
		try {
			lines.addAll(Files.lines(Path.of(filePath)).toList());
		} catch (Exception e) {
			System.out.printf("ERROR: could not read file '%s'.", filePath);
			System.exit(0);
		}
		return lines;
	}
	
	public static void main(final String[] args) {
		var utils = new OPUtils();
		List<String> lines = utils.getLines("~/RESEARCH/neem.oc");
		utils.genJava(lines);
		utils.genCPP(lines);
	}
}
