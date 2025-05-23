package maven;

import java.io.File;
import java.util.ArrayList;

public class Main {
	public static void main(final String[] args) throws Exception {
		java.util.List<Integer> ints = new ArrayList<>();
		ints.add(100);
		ints.addAll(null);
		System.out.println(ints);
		
	}
}
