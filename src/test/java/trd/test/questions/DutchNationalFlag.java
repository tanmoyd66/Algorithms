package trd.test.questions;

import trd.algorithms.utilities.ArrayPrint;

public class DutchNationalFlag {
	public static void Swap(Integer[] A, int i, int j) {
		int temp = A[i]; A[i] = A[j]; A[j] = temp;
	}
	public static void Sort(Integer[] A) {
		int e0 = -1, s2 = A.length, i = 0;
		while (i < s2) {
			switch (A[i]) {
			case 0: Swap(A, ++e0, i); i++; break; 
			case 1: ++i; break;
			case 2: Swap(A, --s2, i); break;
			}
		}
	}
	public static void CountSort(Integer[] A) {
		int[] counts = new int[3];
		for (int i = 0; i < A.length; i++)
			counts[A[i]]++;
		for (int i = 1; i < counts.length; i++)
			counts[i] += counts[i - 1];
		for (int i = 0; i < A.length; i++) {
			A[i] = i < counts[0] ? 0 : i < counts[1] ? 1 : 2;
		}
	}
	public static void main(String[] args) {
		Integer[] A = new Integer[] { 2, 1, 0, 1, 2, 0, 1, 2, 0};
		CountSort(A);
		System.out.printf("%s\n", ArrayPrint.ArrayToString("", A));
	}
}
