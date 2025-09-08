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

public class Dates {

	private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	private static class Range {
		private Date start;
		private Date end;
		private int data;

		public Range(Date start, Date end) {
			this.start = start;
			this.end = end;
		}
		
		public Range(Date start, Date end, int data) {
			this.start = start;
			this.end = end;
			this.data = data;
		}

		@Override
		public String toString() {
			return "[" + df.format(start) + " -> " + df.format(end) + "] " + data;
		}
	}

	public List<Range> breakOverlappingRanges(List<Range> ranges) {
		Set<Date> datePoints = new TreeSet<>();
		for (Range range: ranges) {
			datePoints.add(range.start);
			datePoints.add(DateUtils.addDays(range.end, 1));
		}
		List<Date> sortedPoints = new ArrayList<>(datePoints);
		List<Range> result = new ArrayList<>();
		for (int i = 0; i < sortedPoints.size() - 1; i++) {
			Date start = sortedPoints.get(i);
			Date end = DateUtils.addDays(sortedPoints.get(i + 1), -1);
			if (!start.after(end)) {
				for (Range range: ranges) {
					if (!end.before(range.start) && !start.after(range.end)) {
						result.add(new Range(start, end));
						break;
					}
				}
			}
		}
		return result;
	}
	
	public void populate(List<Range> oldRanges, List<Range> newRanges) {
		for (Range newRange: newRanges) {
			Date startNew = newRange.start;
			Date endNew = newRange.end;
			int data = 0;
			for (Range oldRange: oldRanges) {
				Date startOld = oldRange.start;
				Date endOld = oldRange.end;
				if (startNew.compareTo(startOld) >= 0 && startNew.compareTo(endOld) <= 0 && 
					endNew.compareTo(startOld) >= 0 && endNew.compareTo(endOld) <= 0) {
					data = oldRange.data;
				}
			}
			newRange.data = data;
		}
	}

	public static void main(String[] args) throws Exception {
		Dates dates = new Dates();
		List<Range> input = Arrays.asList(
			new Range(df.parse("2024-01-01"), df.parse("2024-01-10"), 10),
			new Range(df.parse("2024-01-05"), df.parse("2024-01-15"), 20),
			new Range(df.parse("2024-01-20"), df.parse("2024-01-25"), 30)
		);
		input.forEach(System.out::println);
		System.out.println();
		List<Range> output = dates.breakOverlappingRanges(input);
		output.forEach(System.out::println);
	}
}
