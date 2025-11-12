package maven;

import java.util.HashSet;

record Data(String word, long count) {}

public class Main {
	public static void main(String[] args) throws Exception {
		var set = new HashSet<Integer>();
		set.add(100);
		set.add(200);
		set.add(300);
		System.out.println(set);
	}
}












