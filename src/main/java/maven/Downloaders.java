package maven;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;

public class Downloaders implements Runnable {

	private String url;
	
	public Downloaders(String url) {
		this.url = url;
	}
	
	@Override
	public void run() {
		try (InputStream inputStream = URI.create(url).toURL().openStream()) {
			String file = url;
			BufferedInputStream in = new BufferedInputStream(inputStream);
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			while (true) {
				int count = in.read(buffer, 0, 1024);
				if (count == -1) break;
				fos.write(buffer, 0, count);
			}
		} catch (Exception e) {
			System.out.println("ERROR: Download failed.");
		}
	}
	
	public static void main(final String[] args) {
		for (String arg: args) {
			var downloaders = new Downloaders(arg);
			Thread thread = new Thread(downloaders);
			thread.start();
		}
	}
}
