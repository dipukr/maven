package maven;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tools {
	public static String sha256(String input) {
		try {
			var digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
			System.out.println(hash.length);
			var hexval = new StringBuilder();

			for (byte b: hash)
				hexval.append(String.format("%02x", b));

			return hexval.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}
