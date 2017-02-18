package trd.algorithms.Arrays;

import java.security.InvalidParameterException;

import trd.algorithms.utilities.ArrayPrint;
import trd.algorithms.utilities.Swapper;
import trd.algorithms.utilities.Swapper.ISwapper;

public class FindDuplicate {
	
	// Given an unsorted array of numbers between 0 and n-1
	// there are some duplicates. Find a duplicate.
	public static Integer Find(Integer[] A, ISwapper<Integer> swapper) {
		
		// Pass 1: Make sure that all numbers are in range
		for (int i = 0; i < A.length; i++) {
			if (A[i] < 0 || A[i] > A.length)
				throw new InvalidParameterException("");
		}

		// Pass 2: For a number to be a duplicate while trying to place it
		// in its position, there will be another identical number in that
		// position.
		for (int i = 0; i < A.length; i++) {
			
			// If A[i] = i, then this number cannot be a duplicate
			// Move to the next number 
			while (A[i] != i) {
				
				// We are trying to place A[i] in its position
				// A[A[i]] is its position 
				// (because A[A[i]] should be A[i] as A[i] should be i)
				// There is another identical number there
				if (A[i] == A[A[i]]) {
					// That is the duplicate
					return A[i];
				}
				
				// Place A[i] in its position
				swapper.swap(A, i, A[i]);
			}
		}
		return -1;
	}
	
	public static void main(String[] args) {
		Integer[] A = new Integer[] { 1, 3, 2, 0, 5, 3 };
		System.out.printf("Array:%s Duplicate: %d\n", 
							ArrayPrint.ArrayToString("", A),
							FindDuplicate.Find(A, new Swapper.SwapperImpl<Integer>()));
	}
}
