package maven;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SchUtils {
	public static Double roundUp(Double value, int decimalCount) {
		if (value != null) {
			BigDecimal bd = BigDecimal.valueOf(value);
			bd = bd.setScale(decimalCount, RoundingMode.HALF_UP);
			return bd.doubleValue();
		}
		return null;
	}
	public static void main(final String[] args) throws Exception {
		List<String> qtms = Files.lines(Path.of("/home/dkumar/sch.data")).toList();
		int blockNo = 1;
//		for (String qtm: qtms) {
//			double quantum = Double.valueOf(qtm);
//			System.out.printf("INSERT INTO injection_schedule_details (avail_quantum, sch_quantum, time_interval_uid, injection_schedule_uid) "
//					+ "VALUES ('%f', '%f', '%d', #####);\n", quantum, quantum, blockNo);
//			blockNo += 1;
//		}
		blockNo = 1;
		for (String qtm: qtms) {
			double quantum = Double.valueOf(qtm);
			quantum = roundUp(quantum - quantum*6.05/100.0, 3);
			System.out.printf("INSERT INTO drawl_schedule_details (quantum_of_power, time_interval_uid, drawl_schedule_uid) "
					+ "VALUES ('%f', '%d', $$$$$);\n", quantum, blockNo);
			blockNo += 1;
		}
	}
}
