package trd.algorithms.Arrays;

import trd.algorithms.utilities.ArrayPrint;
import trd.algorithms.utilities.Swapper.SwapperImpl;

public class DutchNationalFlag {

	// There is a set of 0, 1 and 2s. Sort them in order.
	public static void Sort(Integer[] A) {

		SwapperImpl<Integer> swapper = new SwapperImpl<Integer>();

		// Track the end positions of 0s and 1s and the start position of 2s
		// We actually know that e1 = i - 1 (use that as invariant to not having to track e1)
		int e0 = -1, s2 = A.length;
		
		// i is your running variable
		int i = 0;
		
		// Forward until you reach the first 0
		while (A[i] != 0) i++;
		swapper.swap(A, ++e0, i); i = 1;
		
		// Grow the unknown region until i is greater than s2 (start of 2s)
		while (i < s2) {
			if (A[i] == 0) {
				// If i points to a 0, we swap it to the end of the 0s and move i forward.
				swapper.swap(A, ++e0, i++);
			} else if (A[i] == 1) {
				// If i points to a 1, we just move forward.
				i++;
			} else {
				// This is tricky. You do not want to move forward i since you do not know 
				//    if i is pointing to a 0. In that case we need another swap to position that right.
				swapper.swap(A, i, --s2);
			}
		}
		
		System.out.printf("%s\n", ArrayPrint.ArrayToString("Dutch National Flag:", A));
	}

	public static void main(String[] args) {
		if (true) {
			DutchNationalFlag.Sort(new Integer[] {1, 0, 2});
			DutchNationalFlag.Sort(new Integer[] {0, 1, 2});
			DutchNationalFlag.Sort(new Integer[] {1, 2, 0});
			DutchNationalFlag.Sort(new Integer[] {1, 0, 1, 1, 2, 1, 2, 0});
		}
	}
}
