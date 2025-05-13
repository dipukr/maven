package maven;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.time.DateUtils;

public final class Dates {

	private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	private static class DateRange {
		private Date start;
		private Date end;
		private int data;

		public DateRange(Date start, Date end) {
			this.start = start;
			this.end = end;
		}
		
		public DateRange(Date start, Date end, int data) {
			this.start = start;
			this.end = end;
			this.data = data;
		}

		@Override
		public String toString() {
			return "[" + df.format(start) + " -> " + df.format(end) + "] " + data;
		}
	}

	public static List<DateRange> breakOverlappingRanges(List<DateRange> ranges) {
		Set<Date> datePoints = new TreeSet<>();
		for (DateRange range : ranges) {
			datePoints.add(range.start);
			datePoints.add(DateUtils.addDays(range.end, 1));
		}
		List<Date> sortedPoints = new ArrayList<>(datePoints);
		List<DateRange> result = new ArrayList<>();
		for (int i = 0; i < sortedPoints.size() - 1; i++) {
			Date from = sortedPoints.get(i);
			Date to = DateUtils.addDays(sortedPoints.get(i + 1), -1);
			if (!from.after(to)) {
				for (DateRange r : ranges) {
					if (!to.before(r.start) && !from.after(r.end)) {
						result.add(new DateRange(from, to));
						break;
					}
				}
			}
		}
		return result;
	}
	
	public static void populate(List<DateRange> oldRanges, List<DateRange> newRanges) {
		for (DateRange dateRangeNew: newRanges) {
			Date startNew = dateRangeNew.start;
			Date endNew = dateRangeNew.end;
			int data = 0;
			for (DateRange dateRangeOld: oldRanges) {
				Date startOld = dateRangeOld.start;
				Date endOld = dateRangeOld.end;
				if (startNew.compareTo(startOld) >= 0 && startNew.compareTo(endOld) <= 0 && 
					endNew.compareTo(startOld) >= 0 && endNew.compareTo(endOld) <= 0) {
					data = dateRangeOld.data;
					//break;
				}
			}
			dateRangeNew.data = data;
		}
	}
	
	public static void test0() throws Exception {
		List<DateRange> input = Arrays.asList(
			new DateRange(df.parse("2024-01-01"), df.parse("2024-01-10")),
			new DateRange(df.parse("2024-01-05"), df.parse("2024-01-15")),
			new DateRange(df.parse("2024-01-20"), df.parse("2024-01-25"))
		);
		input.forEach(System.out::println);
		System.out.println();
		List<DateRange> output = breakOverlappingRanges(input);
		output.forEach(System.out::println);
	}
	
	public static void test1() throws Exception {
		List<DateRange> input = Arrays.asList(
			new DateRange(df.parse("2024-01-01"), df.parse("2024-01-10"), 100),
			new DateRange(df.parse("2024-01-07"), df.parse("2024-01-08"), 200)
		);
		input.forEach(System.out::println);
		System.out.println();
		List<DateRange> output = breakOverlappingRanges(input);
		populate(input, output);
		output.forEach(System.out::println);
	}
	
	public static void test2() throws Exception {
		List<DateRange> input = Arrays.asList(
			new DateRange(df.parse("2025-03-01"), df.parse("2025-03-01"), 100),
			new DateRange(df.parse("2025-03-01"), df.parse("2025-03-01"), 200)
		);
		input.forEach(System.out::println);
		System.out.println();
		List<DateRange> output = breakOverlappingRanges(input);
		populate(input, output);
		output.forEach(System.out::println);
	}

	public static void main(final String[] args) throws Exception {
		//test0();
		//System.out.println();
		test2();
	}
}
