package trd.algorithms.datastructures;

public class IntervalTree {

	public static class Interval {
		Integer start, end;
		Integer max;
		public Interval(int start, int end) {
			this.start = start; this.end = end;
		}
		
		Interval root = null;
		
	}
}
