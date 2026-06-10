package maven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class Coll {
	public static void main(String[] args) throws Exception {
		if (args.length != 1) return;
		var crc32 = new CRC32();
		var file = new File(args[0]);
		var fileReader = new FileReader(file);
		var reader = new BufferedReader(fileReader);
		String line;
		while ((line = reader.readLine()) != null) {
		    crc32.update(line.getBytes(StandardCharsets.UTF_8));
		}
		int checksum = (int) crc32.getValue();
		reader.close();
		System.out.println(checksum);
	}
}
