package maven;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Brute {
	public static void apply(String password, PrintWriter writer) throws Exception {
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec("/home/dkumar/auth");
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
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.lines(Paths.get("/home/dkumar/dict"))
			.collect(Collectors.toList());
		PrintWriter wr = new PrintWriter("/home/dkumar/dumps");
		for (String line: lines)
			apply(line, wr);
		wr.close();
	}
}