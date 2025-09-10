package maven;

import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;

public class Download {

	private static final int MAX_BUFFER_SIZE = 1024;

	private URL url;
	private int size;
	private int count;

	public Download(URL url) {
		this.url = url;
		this.size = -1;
		this.count = 0;
	}

	public void download() {
		try (var file = new RandomAccessFile(getFileName(), "rw")) {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Range", String.format("bytes=%d-", count));
			connection.connect();
			this.size = connection.getContentLength();
			if (size < 1) Error.fatal("Fatal error.");
			file.seek(count);
			InputStream stream = connection.getInputStream();
			byte[] buffer = new byte[MAX_BUFFER_SIZE];
			while (true) {
				int read = stream.read(buffer, 0, buffer.length);
				if (read == -1) break;
				file.write(buffer, 0, read);
				this.count += read;
			}
		} catch (IOException e) {
			Error.fatal("Could not download file " + getFileName());
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public String getUrl() {
		return url.toString();
	}
	
	public double getProgress() {
		return ((double) count / size) * 100;
	}
	
	public String getFileName() {
		return url.getFile().substring(1);
	}

	public static void main(String[] args) throws Exception {
		String addr = "https://stackoverflow.com/questions/tagged/python";
		URL url = new URL(addr);
		Download downloader = new Download(url);
		downloader.download();
	}
}