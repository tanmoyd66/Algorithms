package trd.algorithms.Arrays;

import java.util.Arrays;

import trd.algorithms.utilities.ArrayPrint;

// Defined as:
//		HIndex = N => There are N papers that have greater than N citations 
//		What we are looking for is largest x st: A[x] >= x after sorting
public class HIndex {
	
	// Sort and apply the stopping criterion
	public static int Find_NLogN(Integer[] C) {
		Arrays.sort(C);
	    for (int i = 0; i< C.length; i++){
	        if (C[i] >= C.length - i) 
	        	return C.length - i;
	    }
	    return 0;
	}
	
	// Do the equivalent of BucketSort
	public static int Find_N(Integer[] C) {
		int[] count = new int[C.length + 1];
		
		// count[i] = # of papers with # citations = i   
		for (int i = 0; i < C.length; i++) {
			if (C[i] > C.length)
				count[C.length]++;
			else
				count[C[i]]++;
		}
		
		// total tracks # of papers with citations at least i
		// we need to find the largest such i
		int total = 0;
		for (int i = C.length; i>= 0; i--) {
			total += count[i];
			if (total >= i)
				return i;
		}
		return 0;
	}
	
	public static void main(String[] args) {
		Integer[] A;
		A = new Integer[] {4, 0, 6, 1, 5, 3};
		System.out.printf("%s:",ArrayPrint.ArrayToString("", A));
		System.out.printf("NLogN: %d ", Find_NLogN(A));
		A = new Integer[] {3, 0, 6, 1, 5, 3};
		System.out.printf("N: %d\n", Find_N(A));
	}
}
