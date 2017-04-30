package trd.test.questions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssortedArray {
	public static void Swap(Integer[] A, int i, int j) {
		Integer temp = A[i];
		A[i] = A[j];
		A[j] = temp;
	}
	
	public static void Heapify(Integer[] A, int i, int end) {
		int lChild = 2 * i + 1;
		int rChild = 2 * i + 2;
		if (lChild <= end && A[i] < A[lChild]) {
			Swap(A, i, lChild);
			Heapify(A, lChild);
		}
		if (rChild <= end && A[i] < A[rChild]) {
			Swap(A, i, rChild);
			Heapify(A, rChild);
		}
	}
	public static void Heapify(Integer[] A, int end) {
		for (int i = (end + 1)/2; i >= 0; i--) {
			Heapify(A, i, end);
		}
	}
	
	public static void HeapSort(Integer[] A) {
		for (int i = A.length - 1; i >= 0; i--) {
			Heapify(A, i);
			Swap(A, 0, i);
		}
	}
	
	public static void main(String[] args) {
		Integer[] A = new Integer[] {10, 3, 4, 2, 1, 11, 15, 17, 6, 5};
		List<Integer> Before = new ArrayList<>(Arrays.asList(A));
		HeapSort(A);
		List<Integer> After = Arrays.asList(A);
		System.out.printf("%s:%s\n", Before, After);
	}
}
