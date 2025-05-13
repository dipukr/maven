package maven;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class Loader {
	public static void main(final String[] args) throws Exception {
		URL url = URI.create("https://www.sfml-dev.org/index.php").toURL();
		InputStream inputStream = url.openStream();
		String file = url.getFile().substring(1);
		BufferedInputStream in = new BufferedInputStream(inputStream);
		FileOutputStream fos = new FileOutputStream(file);
		byte[] buffer = new byte[1024];
		while (true) {
			int count = in.read(buffer, 0, 1024);
			if (count == -1) break;
			fos.write(buffer, 0, count);
		}
		fos.close();
		System.out.println("Downloaded.");
	}
}