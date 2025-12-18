package maven;

import java.util.HashMap;
import java.util.TreeMap;

public class Coll {
	public static void main(String[] args) throws Exception {
		var data = new TreeMap<Integer, String>();
		data.put(1, "one");
		data.put(2, "two");
		data.put(3, "three");
		data.put(4, "four");
		data.put(5, "five");
		data.put(6, "six");
		data.put(7, "seven");
		data.put(8, "eight");
		data.put(9, "nine");
		var tbl = new HashMap<Integer, String>();
		tbl.put(1, "one");
		tbl.put(2, "two");
		tbl.putIfAbsent(2, "two1");
		System.out.println(tbl.get(2));
	}
}
