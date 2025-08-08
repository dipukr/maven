package maven;

public class Console {
	public static void log(int data)      {System.out.print(data);}
	public static void log(double data)   {System.out.print(data);}
	public static void log(String data)   {System.out.print(data);}
	public static void log(String format, Object ... args) {System.out.printf(format, args);}
}
