package maven;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Passowrd {
	public void apply(String exePath, String dictPath) throws Exception {
		FileWriter writer = new FileWriter("/home/dkumar/dict.log");
		List<String> passwords = Files.lines(Path.of(dictPath))
				.toList();
		Runtime runtime = Runtime.getRuntime();
		for (String password: passwords) {
			Process process = runtime.exec(exePath.toString());
			InputStream inputStream = process.getInputStream();
			OutputStream outputStream = process.getOutputStream();
			outputStream.write(password.getBytes());
			outputStream.close();
			byte[] data = inputStream.readAllBytes();
			inputStream.close();
			process.waitFor();
			writer.write(password + ": " + new String(data));
		}
		writer.flush();
		writer.close();
	}
	
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		System.out.println(System.getenv("HOME"));
		String exe = "/home/dkumar/collection/auth";
		String dict = "/home/dkumar/Data/words";	
		var algo = new Passowrd();
		algo.apply(exe, dict);
		System.out.println(System.currentTimeMillis() - start);
	}
}
