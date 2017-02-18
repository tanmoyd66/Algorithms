package trd.algorithms.Arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import trd.algorithms.utilities.ArrayPrint;

// Given an array S of n integers, find 3 integers in S that
// equals a number - target.
public class ThreeSumClosest {
	public static List<Integer> FindSum(Integer[] A, Integer target) {
		List<Integer> ret = new ArrayList<Integer>();
		
		// Boundary condition
		if (A.length < 3) {
			for (Integer i : A)
				ret.add(i);
		} else {
			
			// First sort the array O(N Log N)
			Arrays.sort(A);
			
			// O(n^2) clever trick.
			// Fix i
			// Vary j from (i+1)  and k from (A.length-1) from both ends
			// If the sum is larger than target, then decrement k
			// If the sum is smaller, increment i
			int maxSum = A[0] + A[1] + A[2];
			for (int i = 0; i < A.length - 2; i++) {
				int j = i + 1;
				int k = A.length - 1;
				while (j < k) {
					int currSum = A[i] + A[j] + A[k];
					
					// Check which one is closer
					if (Math.abs(target - maxSum) > Math.abs(target - currSum)) {
						
						// if the currentSum is closer, set that to be the max Sum
						maxSum = currSum;
						ret.clear(); ret.add(A[i]); ret.add(A[j]); ret.add(A[k]);
					} else if (currSum > target)
						k--;
					else 
						j++;
				}
			}
		}
		return ret;
	}

	public static void main(String[] args) {
		Integer[] A = new Integer[] { 1, 2, -1, -4 };
		System.out.printf("Max sum in %s close to %d is %s\n", ArrayPrint.ArrayToString("", A), 1, 
							ThreeSumClosest.FindSum(A, 1));
	}
}
