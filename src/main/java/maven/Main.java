package maven;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Main {
	public static void main(final String[] args) throws Exception {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date date = df.parse("13-02-2025");
		LocalDate ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		System.out.println(ld);
		System.out.println(ld.getDayOfMonth());
		System.out.println(ld.lengthOfMonth());
		ld = ld.plusDays(ld.lengthOfMonth() - ld.getDayOfMonth() + 1);
		System.out.println(ld);
	}
}
