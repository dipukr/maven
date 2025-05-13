package maven;

<<<<<<< HEAD
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
	public static void main(final String[] args) throws Exception {
		File file = new File("/etc/");
		System.out.println(file.isDirectory());
		System.out.println(file.isAbsolute());
		System.out.println(file.getAbsolutePath());
		System.out.println(file.getCanonicalPath());
	}
}
