package maven;

public final class Error {
	public static void error(String message) {
		System.out.printf("ERROR: %s.\n", message);
		System.exit(1);
	}
	public static void error(String message, int lineNo) {
		System.out.printf("ERROR: %s on line %d.\n", message, lineNo);
		System.exit(1);
	}
}
