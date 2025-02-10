package maven;

import java.util.TreeSet;
import com.github.javafaker.Faker;

class Person {
	String firstName;
	String lastName;
	String gender;
	String email;
}

public class Fakers {
	public static void main(final String[] args) {
		var faker = new Faker();
		var set = new TreeSet<String>();
		for (int i = 0; i < 100; i++)
			set.add(faker.animal().name());
		System.out.println(set.size());
	}
}
