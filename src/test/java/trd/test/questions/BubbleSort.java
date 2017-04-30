package trd.test.questions;

import trd.algorithms.utilities.ArrayPrint;

public class BubbleSort {
	public static void Swap(Integer[] A, int i, int j) {
		Integer temp = A[i]; A[i] = A[j]; A[j] = temp;
	}
	public static final void Sort(Integer[] A) {
		for (int i = A.length - 1; i >= 1; i--) {
			for (int j = 0; j < i; j++) {
				if (A[j] > A[j + 1])
					Swap(A, j, j + 1); 
			}
		}
	}
	public static void main(String[] args) {
		Integer[] A = new Integer[] {10, 3, 4, 2, 1, 11, 15, 17, 6, 5};
		Sort(A);
		System.out.printf("%s\n", ArrayPrint.ArrayToString("", A));
	}
}
