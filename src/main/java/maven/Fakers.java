package maven;

import java.util.ArrayList;
import java.util.Locale;

import com.github.javafaker.Faker;

public class Fakers {
	public static void main(final String[] args) {
		var faker = new Faker(new Locale("hindi"));
		var list = new ArrayList<String>();
		for (int i = 0; i < 5000; i++)
			list.add(faker.name().lastName());
		System.out.println(list);
	}
}
