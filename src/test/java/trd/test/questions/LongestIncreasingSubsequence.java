package trd.test.questions;

import java.util.LinkedList;

public class LongestIncreasingSubsequence {
	public static void Find(Integer[] A) {
		Integer[] T = new Integer[A.length];
		Integer[] P = new Integer[A.length];
		
		for (int i = 0; i < A.length; i++) {
			T[i] = 1; P[i] = -1;
		}
		
		for (int i = 1; i < A.length; i++) {
			for (int j = 0; j < i; j++) {
				if (A[i] > A[j]) {
					T[i] += 1;
					P[i] = j;
				}
			}
		}
		
		int maxIdx = A.length - 1;
		for (int i = A.length - 1; i >= 0; i--) {
			if (T[i] > T[maxIdx])
				maxIdx = i;
		}
		
		LinkedList<Integer> ll = new LinkedList<Integer>();
		for (int i = maxIdx; i >= 0 && P[i] != -1; i = P[i]) {
			ll.addFirst(A[i]);
		}
		
		System.out.printf("%s\n", ll);
	}
	
	public static void main(String[] args) {
		Find(new Integer[] {1, 2, 4, 3, 2, 3, 5, 6, 1});
	}
}
