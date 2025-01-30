package maven;

import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(final String[] args) throws Exception {
		List<Integer> ints = new ArrayList<>();
		ints.add(100);
		ints.add(200);
		ints.add(300);
		System.out.println(ints);
		for (Integer elem: ints) {
			System.out.println(elem);
			ints.remove((Object)300);
		}
		System.out.println(ints);
	}
}
