package trd.algorithms.Arrays;

public class MinMaxByDivideAndConquer {
	// O(3n/2 - 2)
	public static <T extends Comparable<T>> T Min(T[] A, int start, int end) {
		if (start > end)
			return null;
		if (start == end)
			return A[start];
		
		int mid  = (start + end) >> 1;
		T   minL = Min(A, start, mid);
		T   minR = Min(A, mid + 1, end);

		if (minL != null && minR != null)
			return minL.compareTo(minR) < 0 ? minL : minR;
		else if (minL == null)
			return minR;
		else
			return minL;
	}
	public static <T extends Comparable<T>> T Max(T[] A, int start, int end) {
		if (start > end)
			return null;
		if (start == end)
			return A[start];
		
		int mid  = (start + end) >> 1;
		T   minL = Max(A, start, mid);
		T   minR = Max(A, mid + 1, end);

		if (minL != null && minR != null)
			return minL.compareTo(minR) > 0 ? minL : minR;
		else if (minL == null)
			return minR;
		else
			return minL;
	}
	public static void main(String[] args) {
		Integer A[] = new Integer[] {3, 4, 5, 1, 4, 2, 1, -1};
		System.out.printf("%d %d\n", Min(A, 0, A.length - 1), Max(A, 0, A.length - 1));
	}
}
