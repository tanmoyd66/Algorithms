package trd.algorithms.sorting;

import trd.algorithms.datastructures.HeapOnList;
import trd.algorithms.datastructures.HeapOnList.HeapType;

public class OnlineMedianFinder<T extends Comparable<T>> {
	// Strategy:
	//		Keep 2 heaps - a min-heap (upper)
	//					 - a max-heap (lower)
	// 		When a new element comes:
	HeapOnList<T> maxFirst  = new HeapOnList<T>(HeapType.Max, 20, (a,b)-> a.compareTo(b));
	HeapOnList<T> minSecond = new HeapOnList<T>(HeapType.Min, 20, (a,b)-> a.compareTo(b));
	
	// Push into the minHeap. Take the top of the min and push into maxHeap
	public void Insert(T elem) {
		minSecond.Insert(elem);
		if (Math.abs(minSecond.size() - maxFirst.size()) > 1) {
			T topOfMin = minSecond.ExtractTop();
			maxFirst.Insert(topOfMin);
		}
	}
	
	public T getMedian() {
		if (minSecond.size() > maxFirst.size())
			return minSecond.PeekTop();
		else {
			return maxFirst.PeekTop();
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{"); sb.append(maxFirst);  sb.append("}");
		sb.append("{"); sb.append(minSecond); sb.append("}");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		Integer[] data = new Integer[] {4, 10, 19, 2, 14, 11, 3, 12, 0, 5, 2, 2, 17, 4, 14, 15, 4, 9, 15, 14};
		OnlineMedianFinder<Integer> omf = new OnlineMedianFinder<Integer>();
		for (int i = 0; i < data.length; i++) {
			omf.Insert(data[i]);
			if (i % 5 == 0) {
				System.out.printf("Median after %d: %s %s\n", i, omf.getMedian(), omf);
			}
		}
	}
}
