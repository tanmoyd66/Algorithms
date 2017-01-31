package trd.algorithms.DynamicProgramming;

import java.util.List;

import trd.algorithms.linkedlists.SinglyLinkedList;
import trd.algorithms.utilities.ArrayPrint;

public class LongestIncreasingSubsequences {
	
	// Recursive formula is:
	//		T[i] = max(T[j] + 1, T[i]) for all j < i
	// T[i] tracks what is the largest increasing subsequence that ends at i
	public static Integer[] FindLIS(Integer[] A) {
		
		// Build and initialize cost array and the parent array
		Integer[] T = new Integer[A.length];	// Tracks LIS till i
		Integer[] P = new Integer[A.length];	// Tracks the parent of i
		for (int i = 0; i < A.length; i++) {
			T[i] = 1; P[i] = null;
		}
		
		// now iterate over every element and get the LIS value
		for (int i = 1; i < A.length; i++) {
			for (int j = 0; j < i; j++) {
				if (A[i] > A[j]) {
					if (T[j] + 1 > T[i]) {
						T[i] = T[j] + 1;
						P[i] = j;
					}
				}
			}
		}
		
		// Start from right and find the longest sequence
		int LISEndIndex = A.length - 1, LISLength = T[LISEndIndex];
		for (int i = LISEndIndex - 1; i >= 0; i--) {
			if (T[i] > LISLength) {
				LISEndIndex = i; LISLength = T[LISEndIndex];
			}
		}
		
		// Retrace the index
		Integer[] ret = new Integer[LISLength];
		int j = LISLength;
		for (Integer i = LISEndIndex; i != null; i = P[i]) {
			ret[--j] = A[i]; 
		}
		
		return ret;
	}
	
	// Strategy:
	//		We will keep 2 cost matrices 
	//		TF: will keep the LIS for forward
	//		TB: will keep the LIS for backward
	//		TF = TF + TB
	public static List<Integer> FindBitonicLIS(Integer[] A) {
		Integer[] TF = new Integer[A.length];	// Tracks LIS till i
		Integer[] PF = new Integer[A.length];	// Tracks the parent of i
		Integer[] TB = new Integer[A.length];	// Tracks LIS till i
		Integer[] PB = new Integer[A.length];	// Tracks the parent of i
		for (int i = 0; i < A.length; i++) {
			TF[i] = TB[i] = 1;
			PF[i] = PB[i] = null;
		}
		
		// now iterate over every element and set LIS value (forward)
		for (int i = 1; i < A.length; i++) {
			for (int j = 0; j < i; j++) {
				if (A[i] > A[j]) {
					if (TF[j] + 1 > TF[i]) {
						TF[i] = TF[j] + 1;
						PF[i] = j;
					}
				}
			}
		}

		// now iterate over every element and set LIS value (backward)
		for (int i = A.length - 1; i >=0; i--) {
			for (int j = A.length - 1; j > i; j--) {
				if (A[i] > A[j]) {
					if (TB[j] + 1 > TB[i]) {
						TB[i] = TB[j] + 1;
						PB[i] = j;
					}
				}
			}
		}
		
		// Accumulate the values
		for (int i = 0; i < A.length; i++)
			TF[i] = TB[i] + TF[i] - 1;
		
		// Start from right and find the longest sequence
		int LISMidIndex = A.length - 1, LISLength = TF[LISMidIndex];
		for (int i = LISMidIndex - 1; i >= 0; i--) {
			if (TF[i] > LISLength) {
				LISMidIndex = i; LISLength = TF[LISMidIndex];
			}
		}
		
		// Retrace the index
		SinglyLinkedList<Integer> ret = new SinglyLinkedList<Integer>();
		int j = LISMidIndex;
		while (PB[j] != null) {
			ret.insertTail(A[PB[j]]);
			j = PB[j];
		}	
		j = LISMidIndex;
		while (PF[j] != null) {
			ret.insertHead(A[PF[j]]);
			j = PF[j];
		}	
		return ret.getArray();
	}
	
	public static void main(String[] args) {
		Integer[] 		A 	 = { 3, 4, -1, 0, 6, 2, 3 };
		Integer[] 		LIS  = FindLIS(A);
		List<Integer> 	BLIS = FindBitonicLIS(A);
		System.out.printf("Longest Increasing Subsequence of %s is %s\n", ArrayPrint.ArrayToString("", A), ArrayPrint.ArrayToString("", LIS));
		System.out.printf("Longest Bitonic    Subsequence of %s is :%s\n", ArrayPrint.ArrayToString("", A), BLIS);
	}
}
