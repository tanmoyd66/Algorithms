package trd.algorithms.datastructures;

public class IntervalTree {

	public static class Interval implements Comparable<Interval> {
		Integer start, end;
		public Interval(int start, int end) {
			this.start = start; this.end = end;
		}
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%d,%d", start,end));
			return sb.toString();
		}
		public int compareTo(Interval o) {
			return start.compareTo(o.start);
		}
		public boolean subsumedBy(Interval that) {
			if (that.start <= this.start &&
				that.end >= this.end)
				return true;
			return false;
		}
		public boolean equals(Interval that) {
			return this.start == that.start && this.end == that.end;
		}
	}
	
	public static class IntervalTreeNode {
		Interval interval;
		Integer	 maxEnd;
		IntervalTreeNode left;
		IntervalTreeNode right;
		public IntervalTreeNode(Interval interval) {
			this.interval = interval;
			this.maxEnd = interval.end;
		}
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			if (left != null)
				sb.append(left.toString());
			sb.append(String.format("{%s:%s}", interval, maxEnd));
			if (right != null)
				sb.append(right.toString());
			sb.append("]");
			return sb.toString();
		}
	}

	IntervalTreeNode root = null;
	
	// Recursively insert
	public IntervalTreeNode Insert(IntervalTreeNode itn, Interval key) {
		if (itn == null) {
			// if root is null create new root
			return new IntervalTreeNode(key);
		} else {
			int cmp = key.compareTo(itn.interval);
			if (cmp == 0 && key.equals(itn.interval)) {
				return itn;
			} else if (cmp <= 0) {
				itn.left = Insert(itn.left, key);
			} else {
				itn.right = Insert(itn.right, key);
			}
			itn.maxEnd = Math.max(itn.maxEnd, key.end);
			return itn;
		}
	}
	
	// Search for an exact match interval
	public IntervalTreeNode Search(IntervalTreeNode itn, Interval key) {
		if (itn == null)
			return null;
		if (itn.interval.equals(key))
			return itn;
		int cmp = key.compareTo(itn.interval);
		if (cmp < 0)
			return Search(itn.left, key);
		return Search(itn.right, key);
	}
	
	// Search for interval that overlaps the interval as argument
	public Interval SearchOverlapping(IntervalTreeNode itn, Interval key) {
		if (itn == null)
			return null;
		
		if (key.subsumedBy(itn.interval))
			return itn.interval;
	
		// Go left if the maximal end in the left is greater than the key's end
		if (itn.left != null && itn.left.maxEnd >= key.start)
			return SearchOverlapping(itn.left, key);
		
		// Go right otherwise
		return SearchOverlapping(itn.right, key);
	}
	
	public static void main(String[] args) {
		IntervalTree IT = new IntervalTree();
		Interval[] intervals = new Interval[] {
									new Interval(15, 20), new Interval(10, 30), new Interval(17, 19), 
									new Interval(5 , 20), new Interval(12, 15), new Interval(30, 40), 
		};
		for (Interval i : intervals)
			IT.root = IT.Insert(IT.root, i);
		
		System.out.printf("%s\n", IT.root);
		
		Interval key = new Interval(6, 7);
		Interval ival= IT.SearchOverlapping(IT.root, key);
		System.out.printf("Interval covering %s is %s\n", key, ival);
	}
}
