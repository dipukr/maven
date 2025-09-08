package maven;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Command {
	public static void main(final String[] args) throws Exception {
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec("find");
		InputStream inputStream = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(isr);
		StringBuilder data = new StringBuilder();
		while (true) {
			String line = reader.readLine();
			if (line == null) break;
			data.append(line).append('\n');
		}
		int retval = process.waitFor();
		if (retval == 0)
			System.out.println(data);
	}
}