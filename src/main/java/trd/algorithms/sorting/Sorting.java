package trd.algorithms.sorting;
import trd.algorithms.datastructures.Heap;
import trd.algorithms.datastructures.Heap.HeapType;
import trd.algorithms.utilities.ArrayPrint;
import trd.algorithms.utilities.Swapper;
import trd.algorithms.utilities.Swapper.ISwapper;
import trd.algorithms.utilities.Tuples;

public class Sorting {
	
	// Simple Selection Sort
	// Strategy:
	//		Go thru all the elements in 2 nested loops
	public static <T extends Comparable<T>> void SelectionSort(T[] A) {
		System.out.printf("Array %s:  -> ", ArrayPrint.ArrayToString("", A));
		Swapper.SwapperImpl<T> swapper = new Swapper.SwapperImpl<T>();

		for (int i = 0; i < A.length - 1; i++) {
			for (int j = i + 1; j < A.length; j++) {
				if (swapper.less(A[j], A[i])) {
					swapper.swap(A, i, j);
				}
			}
		}
		System.out.printf("Sorted by Selection Sort %s in %d swaps, %d compares \n", ArrayPrint.ArrayToString("", A), swapper.SwapCount, swapper.CompareCount);
	}

	// Simple Bubble Sort
	// Strategy:
	//		Make n passes - in each pass position the maximal element in the right-most position
	//		In i th pass, you need to go up only to n - i th element as everything to the right is in place 
	public static <T extends Comparable<T>> void BubbleSort(T[] A) {
		System.out.printf("Array %s:  -> ", ArrayPrint.ArrayToString("", A));
		Swapper.SwapperImpl<T> swapper = new Swapper.SwapperImpl<T>();

		for (int pass = 0; pass < A.length - 1; pass++) {
			for (int i = 1; i < A.length - pass; i++) {
				if (swapper.compare(A[i], A[i-1]) < 0) {
					swapper.swap(A, i, i-1);
				}
			}
		}
		System.out.printf("Sorted by Bubble Sort    %s in %d swaps, %d compares \n", ArrayPrint.ArrayToString("", A), swapper.SwapCount, swapper.CompareCount);
	}


	// Heapsort (same as kth largest - go all the way)
	public static <T extends Comparable<T>> void HeapSort(T[] A) {
		System.out.printf("Array %s:  -> ", ArrayPrint.ArrayToString("", A));
		Swapper.SwapperImpl<T> swapper = new Swapper.SwapperImpl<T>();
		for (int i = 0; i < A.length; i++) {
			for (int j = (A.length - i)/2; j >= 1; j--) {
				Heap.Heapify(A, HeapType.Max, j, A.length - i, swapper);
			}
			swapper.swap(A, 0, A.length - i - 1);
		}
		System.out.printf("Sorted by HeapSort       %s in %d swaps, %d compares\n", ArrayPrint.ArrayToString("", A), swapper.SwapCount, swapper.CompareCount);
	}

	// Single Pivot Partitioning
	// We will position element in A[start] at the right position
	// Strategy:
	//		Let i point to the current position on the left and j to the right
	//		Keep moving i to the right as long as A[i] < A[start], swapping between A[i] and A[j] when A[i] > A[j]
	//		Keep moving j to the left as long as  A[j] > A[start], swapping between A[i] and A[j] when A[i] > A[j]
	//		When i > j, swap A[j] with A[i] - this guarantees that A[start] is in the right position
	public static <T extends Comparable<T>> int SinglePivotPartition(T[] A, int start, int end, ISwapper<T> swapper) {
		int i = start + 1, j = end;

		while (i <= j) {
			for ( ; i <= j && swapper.compare(A[i], A[start]) <= 0; i++); 
			for ( ; i <= j && swapper.compare(A[j], A[start]) >= 0; j--);
			if (i < j)
				swapper.swap(A, j, i);
		}
		swapper.swap(A, start, j);
		return j;
	}
	public static <T extends Comparable<T>>  void QuicksortBySinglePivotPartitioning(T[] A, int start, int end, ISwapper<T> swapper) {
		if (start >= end)
			return;
		if (swapper.less(A[end], A[start])) {
			swapper.swap(A, start, end);
		}
		int mid = SinglePivotPartition(A, start, end, swapper);
		QuicksortBySinglePivotPartitioning(A, start, mid - 1, swapper);
		QuicksortBySinglePivotPartitioning(A, mid + 1, end, swapper);
	}
	public static <T extends Comparable<T>>  void QuicksortBySinglePivotPartitioning(T[] A) {
		System.out.printf("Array %s:  -> ", ArrayPrint.ArrayToString("", A));
		Swapper.SwapperImpl<T> swapper = new Swapper.SwapperImpl<T>();
		QuicksortBySinglePivotPartitioning(A, 0, A.length - 1, swapper);
		System.out.printf("Sorted by Quicksort(SPP) %s in %d swaps, %d compares \n", ArrayPrint.ArrayToString("", A), swapper.SwapCount, swapper.CompareCount);
	}
	
	// Double Pivot Partitioning
	// We will position element in A[start] and A[end] at the right position
	// Strategy:
	//		We will keep 3 pointers (lt, i and gt)
	//			lt - end position for A[start]
	//			i  - currently scanned element
	//			gt - end position for A[end]
	// 		We will continue the process until the i and gt pointers cross
	//		At this point the invariant will be A[start] < A[lt] < A[gt] < A[i] <= A[end]
	//		We will swap (A[start] and A[lt]) and (A[gt] and A[end]) 
	public static <T extends Comparable<T>> Tuples.Pair<Integer, Integer> DualPivotPartition(T[] A, int start, int end, ISwapper<T> swapper) {
				 
		// handle some corner cases
		if (end - start + 1 == 2) {
			if (swapper.less(A[1], A[0])) {
				swapper.swap(A, 0, 1);
			}
			return new Tuples.Pair<Integer, Integer>(start + 0, start + 1);
		} else if (end - start + 1 == 3) {
			if (swapper.less(A[1], A[0])) {
				swapper.swap(A, 0, 1);
			}
			if (swapper.less(A[2], A[1])) {
				swapper.swap(A, 1, 2);
			}
			return new Tuples.Pair<Integer, Integer>(start + 1, start + 2);
		}

		// loop while i and gt does not cross
		int i  = start + 1;	  // tracks current scan head
		int lt = start + 1;   // tracks final position of start
		int gt = end   - 1;	  // tracks final position of end

		while (i <= gt) {
			if (swapper.less(A[i], A[start])) {

				// if A[i] < A[start], we have found an element that is smaller than final position of A[start]
				// swap A[i] with A[lt] and forward lt
				swapper.swap(A, i++, lt++);
			
			} else if (swapper.less(A[end], A[i])) {
			
				// if A[i] > A[end], we have found an element that is greater than final position of A[end]
				// swap A[i] with A[gt] and decrement gt
				swapper.swap(A, gt--, i);

			} else {
				i++;
			}
		}

		swapper.swap(A, start, --lt);
		swapper.swap(A, end,   ++gt );

		return new Tuples.Pair<Integer, Integer>(lt, gt);
	}
	public static <T extends Comparable<T>>  void QuicksortByDualPivotPartitioning(T[] A, int start, int end, ISwapper<T> swapper) {
		if (start >= end)
			return;

		if (swapper.less(A[end], A[start])) {
			swapper.swap(A, start, end);
		}
		
		// Do pivoting only if more than 3 elements 
		Tuples.Pair<Integer, Integer> mid = DualPivotPartition(A, start, end, swapper);
		QuicksortByDualPivotPartitioning(A, start, mid.elem1 - 1, swapper);
		if (swapper.less(A[mid.elem1], A[mid.elem2]))
			QuicksortByDualPivotPartitioning(A, mid.elem1 + 1, mid.elem2 - 1, swapper);
		QuicksortByDualPivotPartitioning(A, mid.elem2 + 1, end, swapper);
	}
	public static <T extends Comparable<T>>  void QuicksortByDualPivotPartitioning(T[] A) {
		System.out.printf("Array %s:  -> ", ArrayPrint.ArrayToString("", A));
		Swapper.SwapperImpl<T> swapper = new Swapper.SwapperImpl<T>();
		QuicksortByDualPivotPartitioning(A, 0, A.length - 1, swapper);
		System.out.printf("Sorted by Quicksort(DPP) %s in %d swaps, %d compares\n", ArrayPrint.ArrayToString("", A), swapper.SwapCount, swapper.CompareCount);
	}

	// Find the kth Largest Using Heap
	// Strategy:
	//		Partial Heap-Sort up to k elements in the end
	//		The k-th element from the end will be your element
	public static <T extends Comparable<T>> T KthLargest_Heap(T[] A, int k) {
		Swapper.SwapperImpl<T> swapper = new Swapper.SwapperImpl<T>();		
		System.out.printf("Array %s: %s a Heap -> ", ArrayPrint.ArrayToString("", A), 
							Heap.IsHeap(A, HeapType.Min, swapper) ? "is" : "is not");
		
		// Do partial Heap-Sort for k-elements, after each Heapify stage, swap the top to the last (so that we can reduce the size by -1)
		for (int i = 0; i < k; i++) {
			for (int j = (A.length - i)/2; j >= 1; j--) {
				Heap.Heapify(A, HeapType.Min, j, A.length - i, swapper);
			}
			swapper.swap(A, 0, A.length - i - 1);
		}
		
		// k-th element from the end will be our element
		System.out.printf("%d-th largest is %s in %d swaps, %d compares\n", k, A[A.length - k], swapper.SwapCount, swapper.CompareCount);
		return A[A.length - k];
	}

	// Find the kth Largest Using Partitioning
	// Strategy: 
	//		Partition the array into 3 partitions using DPP
	// 		Based on the partition sizes check which partition the element will lie in
	//		Recursively search in that partition
	public static <T extends Comparable<T>> T KthLargest_DPP(T[] A, int k, int start, int end, ISwapper<T> swapper) {
		int initialK = k;
		System.out.printf("Array %s:  -> ", ArrayPrint.ArrayToString("", A));
		T retVal = A[0];

if (start == 3 && end == 5) {
	int i = 0;
}
		while (k >= 0) {
			
			// If we have a singleton set we are searching in, k has to be 1
			if (start == end) {
				retVal = k == 1 ? A[start] : null; break;			
			}
			
			// Perform a dual pivot partitioning. This will give us 3 segments (start, mid1-1), (mid1+1, mid2-1), (mid2+1, end)
			Tuples.Pair<Integer, Integer> mid = DualPivotPartition(A, start, end, swapper);

			// Find which of the 3 segments is K in
			if (mid.elem1 > 0 && k < (mid.elem1 - start + 1)) {
				end = mid.elem1;
			} else {
				if (k == (mid.elem1 - start + 1)) {
					retVal = A[mid.elem1]; break;
				} else if (mid.elem2 != null && k < (mid.elem2 - start + 1)) { 
					k -= (mid.elem1 - start + 1);
					start = mid.elem1 + 1; end = mid.elem2 - 1;
				} else if (mid.elem2 != null && k == mid.elem2 - start + 1) {
					retVal = A[mid.elem2]; break;
				} else if (mid.elem2 != null) {
					k -= (mid.elem2 - start + 1);
					start = mid.elem2 + 1;
				} else {
					retVal = null; break;
				}
			}
		}
		System.out.printf("%d-th largest is %s in %d swaps, %d compares\n", initialK, retVal, swapper.getSwapCount(), swapper.getCompareCount());
		return retVal;
	}

	public static void main(String[] args) {
		
		Swapper.SwapperImpl<Integer> swapper = new Swapper.SwapperImpl<Integer>();

		Integer[] A = new Integer[] { 8, 12, 9, 7, 22, 3, 26, 14, 11, 15, 22, 6}; 		
		KthLargest_Heap(A, 5);

		A = new Integer[] { 8, 12, 9, 7, 22, 3, 26, 14, 11, 15, 22, 6}; 		
		KthLargest_DPP(A, 5, 0, A.length - 1, swapper);
		
		HeapSort(new Integer[] { 8, 12, 9, 7, 22, 3, 26, 14, 11, 15, 22, 6});
		SelectionSort(new Integer[] { 8, 12, 9, 7, 22, 3, 26, 14, 11, 15, 22, 6});		
		BubbleSort(new Integer[] { 8, 12, 9, 7, 22, 3, 26, 14, 11, 15, 22, 6});		
		
		A = new Integer[] { 8, 12, 9, 7, 22, 3, 26, 14, 11, 15, 22, 6};
		QuicksortBySinglePivotPartitioning(A);
		QuicksortBySinglePivotPartitioning(A);

		DualPivotPartition(new Integer[] { 8, 12, 9, 7}, 0, 3, new Swapper.SwapperImpl<Integer>());
		A = new Integer[] { 8, 12, 9, 7, 22, 3, 26, 14, 11, 15, 22, 6};
		QuicksortByDualPivotPartitioning(A);
		QuicksortByDualPivotPartitioning(A);
	}
}
