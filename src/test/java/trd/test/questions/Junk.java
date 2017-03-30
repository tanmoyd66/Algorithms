package trd.test.questions;

public class Junk {
	public static Integer FindPeakWithDuplicates(Integer[] A) {
		int start = 0, end = A.length - 1;
		while (start < end) {
			int mid = (start + end) >> 1;
			if (mid == start)
				return start;
			
			if (A[mid - 1] < A[mid]) {
				if (A[mid] < A[mid + 1]) {
					start = mid + 1;
				} else {
					while (A[mid + 1] == A[mid] && mid < end)
						mid++;
					if (mid == end || A[mid + 1] < A[mid])
						return A[mid];
					start = mid + 1;
				}
			}
		}
		return A[start];
	}
	
	public static void main(String[] args) {
		Integer[] A = new Integer[] { 1, 3, 3, 2, 1 };
		System.out.printf("%d\n", FindPeakWithDuplicates(A));
	}
}
