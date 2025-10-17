package maven;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Dict {
	public void dictionaryAttack(String dictPath, String exePath) throws Exception {
		List<String> passwords = Files.lines(Path.of(dictPath)).toList();
		var writer = new FileWriter("/home/dkumar/dict_attack.log");
		Runtime runtime = Runtime.getRuntime();
		for (String password: passwords) {
			Process process = runtime.exec(exePath);
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
		String exePath = "/home/dkumar/collection/auth";
		String dictPath = "/home/dkumar/Data/words";	
		var algo = new Dict();
		algo.dictionaryAttack(dictPath, exePath);
		System.out.println(System.currentTimeMillis() - start);
	}
}
