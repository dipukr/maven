package maven;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
	public static void main(String[] args) throws Exception {
		Path path = Path.of("/home/dkumar/31");
		List<String> lines = Files.lines(path).toList();
		for (int i = 0; i < lines.size(); i++) {
			String[] data = lines.get(i).split(",");
			double qtm = Double.valueOf(data[1]);
			String qry = String.format("INSERT INTO samast.drawl_schedule_details "
					+ "(quantum_of_power, time_interval_uid, drawl_schedule_uid) "
					+ "VALUES (%f, %d, 72009);", qtm, i + 1);
			System.out.println(qry);
		}
	}
}
