package maven;

import java.util.Arrays;
import java.util.TreeMap;

public class Coll {
	public static void main(String[] args) throws Exception {
		String[] ss = "key||value".split("\\|");
		for (String s: ss) System.out.println(s);
		System.out.println(ss.length);
	}
}
