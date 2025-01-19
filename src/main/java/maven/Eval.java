package maven;

import java.util.Random;

public class Eval {
	private static final char[] all = {'0','1','2','3','4','5','6','7','8','9',
									   'a','b','c','d','e','f','g','h','i','j',
									   'k','l','m','n','o','p','q','r','s','t',
									   'u','v','w','x','y','z','A','B','C','D',
									   'E','F','G','H','I','J','K','L','M','N',
									   'O','P','Q','R','S','T','U','V','W','X',
									   'Y','Z'};

	private static boolean alpha(char c) {return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');}
	private static boolean digit(char c) {return (c >= '0' && c <= '9');}
	private static final Random rd = new Random();
	
	private static void test0(char[] chars) {
		long digitCount = 0;
		long alphaCount = 0;
		int a = 0;
		while (a < chars.length) {
			switch (chars[a++]) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9': digitCount++; break;
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'g':
			case 'h':
			case 'i':
			case 'j':
			case 'k':
			case 'l':
			case 'm':
			case 'n':
			case 'o':
			case 'p':
			case 'q':
			case 'r':
			case 's':
			case 't':
			case 'u':
			case 'v':
			case 'w':
			case 'x':
			case 'y':
			case 'z':
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
			case 'G':
			case 'H':
			case 'I':
			case 'J':
			case 'K':
			case 'L':
			case 'M':
			case 'N':
			case 'O':
			case 'P':
			case 'Q':
			case 'R':
			case 'S':
			case 'T':
			case 'U':
			case 'V':
			case 'W':
			case 'X':
			case 'Y':
			case 'Z': alphaCount++; break;
			default: System.out.println("Illegal character found.");
			}
		}
		System.out.printf("Total digit count: %d\n", digitCount);
		System.out.printf("Total alpha count: %d\n", alphaCount);
	}
	
	private static void test1(char[] chars) {
		long digitCount = 0;
		long alphaCount = 0;
		int a = 0;
		while (a < chars.length) {
			char ch = chars[a++];
			if (digit(ch)) digitCount++;
			else if (alpha(ch)) alphaCount++;
		}
		System.out.printf("Total digit count: %d\n", digitCount);
		System.out.printf("Total alpha count: %d\n", alphaCount);
	}
	
	public static void main(final String[] args) {
		char[] chars = new char[500_000_000];
		for (int a = 0; a < chars.length; a++)
			chars[a] = all[rd.nextInt(all.length)];
		long start = System.currentTimeMillis();
		test1(chars);
		long end = System.currentTimeMillis();
		System.out.printf("Millis: %d", end - start);
	}
}
