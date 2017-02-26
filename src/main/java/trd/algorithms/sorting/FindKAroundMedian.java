package trd.algorithms.sorting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import trd.algorithms.datastructures.HeapOnList;
import trd.algorithms.datastructures.HeapOnList.HeapType;

public class FindKAroundMedian<T extends Comparable<T>> {
	public static class HeapElement<T> implements Comparable<HeapElement<T>> {
		T value;
		Integer dist;
		public int compareTo(HeapElement<T> o) {
			return dist.compareTo(o.dist);
		}
		public HeapElement(T value, Integer dist) {
			this.value = value; this.dist = dist;
		}
		public String toString() {
			return String.format("[%s,%d]", value, dist);
		}
	}
	public static <T extends Comparable<T>> List<T> Find(T[] A, int k, 
									BiFunction<T,T,T> medianer,
									BiFunction<T,T,Integer> differer) {
		List<T> ret = new ArrayList<T>();
		
		// Step 1: Find the median
		T median = Sorting.FindMedian(A, medianer);
		
		// Step 2: Build a heap of k elements where heap value = distance from median
		HeapOnList<HeapElement<T>> heap = new HeapOnList<>(HeapType.Max, A.length, (a,b)->a.compareTo(b));
		
		// Step 3; insert the first k elements into the heap
		for (int i = 0; i < k; i++)
			heap.Insert(new HeapElement<T>(A[i], differer.apply(A[i], median)));
		
		// Step 4: For the remaining elements, compare with the max in the heap.
		// If the max element in the heap is greater than A[i], reject it.
		// Otherwise remove the max element and Add[i]
		for (int i = k; i < A.length; i++) {
			HeapElement<T> he = heap.PeekTop();
			Integer diffWithThis = differer.apply(A[i], median);
			if (he.dist.compareTo(diffWithThis) > 0) {
				heap.ExtractTop();
				heap.Insert(new HeapElement<>(A[i], diffWithThis));
			}
		}
		
		// Step 5: Add elements in the heap to this
		for (HeapElement<T> t : heap.getElements())
			ret.add(t.value);
		return ret;
	}
	
	public static void main(String[] args) {
		Integer[] A = new Integer[] { 8, 12, 9, 7, 22, 3, 26, 14, 11, 15, 22, 6}; 		
		List<Integer> aroundMedian = Find(A, 4, (a,b)->(a + b)/2, (a,b)->Math.abs(b-a));
		System.out.println(aroundMedian);
	}
}
