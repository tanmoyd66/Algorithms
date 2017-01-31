package trd.algorithms.Arrays;

import trd.algorithms.utilities.ArrayPrint;
import trd.algorithms.utilities.Tuples;

public class SubArrays {

	// Find the minimum in an array
	public static <T extends Comparable<T>> int min(T[] A, int start, int end) {
		int lastMin = start;
		for (int i = start; i < end; i++) {
			int icmp = A[lastMin].compareTo(A[i]);
			lastMin = icmp > 0 ? i : lastMin;
		}
		return lastMin;
	}

	// Find the maximum in an array
	public static <T extends Comparable<T>> int max(T[] A, int start, int end) {
		int lastMax = start;
		for (int i = start; i < end; i++) {
			int icmp = A[lastMax].compareTo(A[i]);
			lastMax = icmp < 0 ? i : lastMax;
		}
		return lastMax;
	}
	

	// Given an array of numbers find the maximal difference
	// argmax [i<j] (A[j] - A[i])
	// O(n^2) 
	public static void FindMaxDifference_BruteForce(Integer[] A) {
		Tuples.Triple<Integer, Integer, Integer> ret = new Tuples.Triple<Integer, Integer, Integer>(Integer.MIN_VALUE, 0, 0);
		for (int i = 0; i < A.length - 1; i++) {
			for (int j = i + 1; j < A.length; j++) {
				int thisDiff = A[j] - A[i];
				if (thisDiff > ret.e1) {
					ret.e1 = thisDiff;
					ret.e2 = i; ret.e3 = j;
				}
			}
		}
		System.out.printf("%s: %d:[%d-%d]\n", ArrayPrint.ArrayToString("Max Diff:", A), ret.e1, ret.e2, ret.e3);
	}

	public static void FindMaxDifference_DQ(Integer[] A, int start, int end, Tuples.Triple<Integer, Integer, Integer> curr) {
		
		if (start == 4 && end == 7)
			{ int i = 0; }
		
		if (end - start == 1) {
			if (curr.e1 < (A[end] - A[start])) {
				curr.e1 = curr.e1 < (A[end] - A[start]) ? A[end] - A[start] : curr.e1;
				curr.e2 = start;
				curr.e3 = end;
				return;
			} else {
				return;
			}
		} else {
			
			// Divide the range (start, end) into two pieces and do a recursive find in each piece
			int mid = (start + end)/2; 
			if (end/2 > start)
				FindMaxDifference_DQ(A, start, mid , curr);
			Tuples.Triple<Integer, Integer, Integer> retL = new Tuples.Triple<Integer, Integer, Integer>(curr.e1, curr.e2, curr.e3);
			if (end > (end/2 + 1))
				FindMaxDifference_DQ(A, mid + 1, end, curr);
			Tuples.Triple<Integer, Integer, Integer> retR = new Tuples.Triple<Integer, Integer, Integer>(curr.e1, curr.e2, curr.e3);
			
			// O(n) merge between the two halves where start is in left and end is in right
			int minLI = min(A, start, mid);
			int minRI = max(A, mid + 1, end - 1);
			int diff  = A[minRI] - A[minLI];
			
			// Find which one is the maximal
			Integer[] theThree = new Integer[] { diff, retL.e1, retR.e1 };
			int whichIsMax = max(theThree, 0, theThree.length);
			
			switch (whichIsMax) {
				case 0: curr = new Tuples.Triple<Integer, Integer, Integer>(diff, minLI, minRI); return;
				case 1: curr = retL; return;
				case 2: curr = retR; return;
			}
		}
	}	
	public static void FindMaxDifference_DivideAndConquer(Integer[] A) {		
		Tuples.Triple<Integer, Integer, Integer> ret = new Tuples.Triple<Integer, Integer, Integer>(Integer.MIN_VALUE, 0, 0); 
		FindMaxDifference_DQ(A, 0, A.length - 1, ret);
		System.out.printf("%s: %d:[%d-%d]\n", ArrayPrint.ArrayToString("Max Diff DQ:", A), ret.e1, ret.e2, ret.e3);
	}
	
	// Maximal sum sub-array
	public static void MaximalSumSubArray(Integer[] A) {		
		Tuples.Triple<Integer, Integer, Integer> last = new Tuples.Triple<Integer, Integer, Integer>(Integer.MIN_VALUE, 0, 0); 
		int start = Integer.MIN_VALUE, end = 0, sum = 0;
		while (end < A.length) {
			if (A[end] < 0) {
				if (sum > last.e1) {
					last.e1 = sum; last.e2 = start; last.e3 = end - 1;
				}
				sum = 0;
				end++;
				start = end; 
			} else {
				sum += A[end++];
			}
		}
		if (sum > last.e1) {
			last.e1 = sum; last.e2 = start; last.e3 = end - 1;
		}
		System.out.printf("%s: %d:[%d-%d]\n", ArrayPrint.ArrayToString("Max Sum Sub Array:", A), last.e1, last.e2, last.e3);
	}

	// Maximal sum sub-array
	public static void LongestIncreasingSubArray(Integer[] A) {		
		Tuples.Triple<Integer, Integer, Integer> last = new Tuples.Triple<Integer, Integer, Integer>(Integer.MIN_VALUE, 0, 0); 
		int start = 0, end = 1, currLength = 0;
		while (end < A.length) {
			if (A[end] < A[end - 1]) {
				if (last.e1 < currLength ) {
					last.e2 = start; last.e3 = end - 1; last.e1 = currLength;
				}
				currLength = 1; start = end++;
			} else {
				end++; 
				currLength++;
			}
		}
		
		if ((start < (end - 1)) && (currLength > last.e1)) {
			last.e2 = start; last.e3 = end - 1;
		}
		System.out.printf("%s: %d:[%d-%d]\n", ArrayPrint.ArrayToString("Longest Increasing Sub Array:", A), last.e1, last.e2, last.e3);
	}

	// 
	public static void main(String[] args) {
		if (true) {
			FindMaxDifference_BruteForce(new Integer[] {-10, 11, -8, 8, 6, 12, -11, 30});
			FindMaxDifference_DivideAndConquer(new Integer[] {-10, 11, -8, 8, 6, 12, -11, 30});
			MaximalSumSubArray(new Integer[] {-10, 11, -8, 8, 6, 12, -11, 23});
			LongestIncreasingSubArray(new Integer[] {-10, 11, -8, 8, 6, 12, -11, 23});
		}
	}
}
