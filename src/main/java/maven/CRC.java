package maven;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.CRC32;

public class CRC {
	public static void main(String[] args) throws Exception {
		if (args.length != 1) return;
		var filePath = Path.of(args[0]);
		var crc32 = new CRC32();
		for (String line: Files.readAllLines(filePath, StandardCharsets.UTF_8))
	        crc32.update(line.getBytes(StandardCharsets.UTF_8));
		int checksum = (int) crc32.getValue();
		System.out.printf("Checksum: %d", checksum);
	}
}