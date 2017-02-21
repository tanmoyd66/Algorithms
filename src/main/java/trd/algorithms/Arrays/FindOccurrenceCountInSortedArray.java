package trd.algorithms.Arrays;

import trd.algorithms.utilities.ArrayPrint;

public class FindOccurrenceCountInSortedArray {
	public static <T extends Comparable<T>> int Find_N2(T[] A, T target) {
		// Find a match for target.
		// Scan right until there is a match, incrementing count
		int count = -1;
		for (int i = 0; i < A.length; i++) {
			if (A[i].compareTo(target) == 0) {
				count = 1;
				for (int j = i + 1; j < A.length; j++) {
					if (A[j].compareTo(A[i]) == 0)
						count++;
					else
						break;
				}
				break;
			}
		}
		return count;
	}
	
	public static <T extends Comparable<T>> int Find_NLogN(T[] A, T target) {
		
		// The beauty in this is the cascading of the cmp = -1 and cmp = 0 cases
		// Do Binary Search to find the first occurrence
		int start = 0, end = A.length - 1;
		int startIdx = -1;
		while (start <= end) {
			int mid = (start + end)/2;
			int cmp = target.compareTo(A[mid]);
			if (cmp > 0) {
				start = mid + 1;
			} else {	// Look left for equality
				end = mid - 1;
				startIdx = cmp == 0 ? mid : startIdx;
			}
		}

		// Do Binary Search to find the last occurrence
		start = 0; end = A.length - 1;
		int endIdx = -1;
		while (start <= end) {
			int mid = (start + end)/2;
			int cmp = target.compareTo(A[mid]);
			if (cmp < 0) {
				end = mid - 1;
			} else {	// Look right for equality
				start = mid + 1;
				endIdx = cmp == 0 ? mid : endIdx;
			}
		}
	
		// The count is the subtraction of the two indices
		return endIdx - startIdx + 1;
	}
	
	public static void main(String[] args) {
		Integer[] A = new Integer[] {1, 2, 3, 3, 3, 3, 4, 5};
		System.out.printf("In %s, %s occurs %d times (N2), %d times (NLogN)\n", 
					ArrayPrint.ArrayToString("", A), 3, Find_N2(A, 3), Find_NLogN(A, 3));
	}
}
