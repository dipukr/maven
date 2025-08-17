package maven;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Password {
	public static void apply(String password, PrintWriter writer) throws Exception {
		String command = "/home/dkumar/auth";
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(command);
		InputStream inputStream = process.getInputStream();
		OutputStream outputStream = process.getOutputStream();
		outputStream.write(password.getBytes());
		outputStream.close();
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(isr);
		StringBuilder data = new StringBuilder();
		while (true) {
			String line = reader.readLine();
			if (line == null) break;
			data.append(line);
		}
		int retval = process.waitFor();
		reader.close();
		isr.close();
		inputStream.close();
		if (retval == 0)
			writer.println(password + ": " + data);
	}
	
	public static void main(final String[] args) throws Exception {
		Path dictPath = Paths.get("/home/dkumar/dict");
		List<String> lines = Files.lines(dictPath)
			.collect(Collectors.toList());
		File log = new File("/home/dkumar/dumps");
		PrintWriter wr = new PrintWriter(log);
		for (String line: lines)
			apply(line, wr);
		wr.close();
	}
}