package trd.algorithms.datastructures;


import trd.algorithms.utilities.ArrayPrint;
import trd.algorithms.utilities.Swapper;
import trd.algorithms.utilities.Swapper.ISwapper;

public class Heap<T extends Comparable<T>> {
	
	// Heapify an array of n elements (indexed 1..n)
	// The number of leaf nodes in the array will be from (n/2 + 1)...(n). The rest (1..n/2) are internal.
	// The children of i is 2i and 2i+1
	// Heap Property: A[i] <= A[2i] and A[i] <= A[2i+1] (for MaxHeap it is the other way)
	// A Heap is something for which all nodes satisfy that property 
	// We have to do the full subtree every time we enforce the heap property
	public static enum HeapType { Min, Max }

	public static <T extends Comparable<T>> boolean IsHeap(T[] A, HeapType ht, int idx, ISwapper<T> swapper) {
		int root    = idx - 1; 
		int left    = (2 * idx) - 1;
		int right   = (2 * idx + 1) - 1;

		// Check for Heap Invariant property
		if (ht == HeapType.Max) {
			return 	(left  > A.length  || swapper.less(A[left],  A[root])) && 
					(right >= A.length || swapper.less(A[right], A[root]));
		} else {
			return 	(left  > A.length  || swapper.less(A[left],  A[root])) && 
					(right >= A.length || swapper.less(A[right], A[root]));
		}
	}
	public static <T extends Comparable<T>> boolean IsHeap(T[] A, HeapType ht, ISwapper<T> swapper) {
		boolean fIsHeap = true;
		for (int i = A.length/2; fIsHeap && i >= 1; i--) {
			fIsHeap &= IsHeap(A, ht, i, swapper);
		}
		return fIsHeap;
	}
	
	public static <T extends Comparable<T>> void Heapify(T[] A, HeapType ht, int idx, int end, Swapper.SwapperImpl<T> swapper) {
		int 	root    = idx - 1; 
		int 	left    = (2 * idx) - 1;
		int 	right   = (2 * idx + 1) - 1;
		int 	cmp     = 0;
		int 	largest = root;
		
		// Find which one of the (root, left, right) is largest
		// We will swap that with the root and recurse on Heapify to enforce Heap property
		if (left < end) {
			cmp   = swapper.compare(A[left], A[root]);		
			if (ht == HeapType.Max && (cmp > 0) ||
				ht == HeapType.Min && (cmp < 0)) {
				largest = left;
			}
		}
		
		if (right < end) {
			cmp   = swapper.compare(A[right], A[largest]);		
			if (ht == HeapType.Max && (cmp > 0) ||
				ht == HeapType.Min && (cmp < 0)) {
				largest = right;
			}
		}
		if (largest != root) {
			swapper.swap(A, root, largest);
			Heapify(A, ht, largest, end, swapper);
		}
	}
	public static <T extends Comparable<T>> void Heapify(T[] A, HeapType ht) {
		
		Swapper.SwapperImpl<T> swapper = new Swapper.SwapperImpl<T>();
		System.out.printf("Array %s: %s a Heap -> ", ArrayPrint.ArrayToString("", A), IsHeap(A, ht, swapper) ? "is" : "is not");
		
		for (int i = A.length/2; i >= 1; i--) {
			Heapify(A, ht, i, A.length, swapper);
		}
		System.out.printf("%s-Heapified %s: in %d swaps, %d compares\n", ht == HeapType.Max ? "Max" : "Min", ArrayPrint.ArrayToString("", A), swapper.SwapCount, swapper.CompareCount);
	}


}
