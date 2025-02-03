package maven;

public final class Error {
	public static void fatal(String message) {
		System.out.printf("Fatal error: %s\n", message);
		System.exit(1);
	}
	public static void fatal(String message, int lineNo) {
		System.out.printf("Fatal error: %s\n on line %d", message, lineNo);
		System.exit(1);
	}
}
