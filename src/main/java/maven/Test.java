package maven;

public class Test {
	public static String compressedString(String word) {
		var result = new StringBuilder();
		int i = 1, count = 1;
		char prevChar = word.charAt(0);
		while (true) {
			char currChar = word.charAt(i++);
			if (currChar == prevChar && count < 9) {
				count++;
			} else {
				result.append(count);
				result.append(prevChar);
				count = 1;
			}
			if (i == word.length()) {
				result.append(count);
				result.append(currChar);
				break;
			}
			prevChar = currChar;
		}
		return result.toString();
	}
	
	public static void main(final String[] args) {
		System.out.println(compressedString("aaaaaaadddbnmmm"));
	}
}
