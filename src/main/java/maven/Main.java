package maven;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

record Data(String word, long count) {}

public class Main {
	public static void main(String[] args) throws Exception {
		var words = new ArrayList<String>();
		words.add("hello");
		words.add("hello");
		words.add("world");
		int n = 1;
		Map<String, Long> result = words.stream()
				.collect(Collectors.groupingBy(elem-> elem, Collectors.counting()));
		List<Data> datas = result.entrySet().stream()
			.map(elem -> new Data(elem.getKey(), elem.getValue()))
			.sorted(Comparator.comparing(Data::count)).toList();
		System.out.println(datas.get(n - 1));
	}
}
