package maven;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class Loader {
	public static void main(final String[] args) throws Exception {
		long start = System.currentTimeMillis();
		URL url = URI.create("https://www.sfml-dev.org/index.php").toURL();
		InputStream inputStream = url.openStream();
		String file = url.getFile().substring(1);
		BufferedInputStream in = new BufferedInputStream(inputStream);
		FileOutputStream fos = new FileOutputStream(file);
		final int BUF_SZ = 1 << 12;
		byte[] buffer = new byte[BUF_SZ];
		while (true) {
			int count = in.read(buffer, 0, BUF_SZ);
			if (count == -1) break;
			fos.write(buffer, 0, count);
		}
		fos.close();
		long end = System.currentTimeMillis();
		System.out.printf("Downloaded in %d millis.", end - start);
	}
}