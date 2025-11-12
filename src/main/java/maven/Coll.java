package maven;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Coll {
	public static String longestString(String[] strs) {
		Map<String, Integer> strToLen = Stream.of(strs)
				.collect(Collectors.toMap(Function.identity(), String::length));
		return strToLen.entrySet().stream()
				.sorted(Collections.reverseOrder(Entry.comparingByValue()))
				.findFirst()
				.get()
				.getKey();
	}
	public static void main(String[] args) throws Exception {
		String[] data= {"hello", "hi", "wordldf!", "dep"};
		System.out.println(longestString(data));
	}
}
