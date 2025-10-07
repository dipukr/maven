package maven;

import java.io.File;

public class Main {
	public static void main(String[] args) throws Exception {
		File file = new File("pom.xml");
		System.out.println(file.getName());
		System.out.println(file.getAbsolutePath());
		System.out.println(file.getParent());
	}
}
