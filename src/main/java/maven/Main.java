package maven;

import java.io.RandomAccessFile;

public class Main {
	public static void main(final String[] args) throws Exception {
		var rand = new RandomAccessFile("/home/dkumar/codes/data", "rw");
		long sz = rand.length();
		long mid = sz / 2;
		rand.seek(mid);
		rand.write("ok".getBytes());
		rand.close();
	}
}
