package trd.algorithms.datastructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.function.BiFunction;

public class HeapOnList<T extends Comparable<T>> {
	public enum HeapType { Min, Max };

	int initialSize = 10;
	HeapType ht;
	ArrayList<T> elements = new ArrayList<T>(initialSize);
	BiFunction<T,T,Integer> comparer;

	public void SwapElements(int i, int j) {
		T temp = elements.get(j); 
		elements.set(j, elements.get(i)); 
		elements.set(i, temp); 
	}
	
	public HeapOnList(HeapType ht, int initialSize, BiFunction<T,T,Integer> comparer) {
		if (initialSize > 0)
			this.initialSize = initialSize;
		this.ht = ht;
		this.comparer = comparer;
	}
	
	public void HeapifyOne(int i) {
		int leftCIdx  = 2 * i + 1;
		int rightCIdx = 2 * i + 2;
		
		if (leftCIdx < elements.size()) {
			int cmp = comparer.apply(elements.get(i), elements.get(leftCIdx));
			if (ht == HeapType.Min && cmp > 0 ||
				ht == HeapType.Max && cmp < 0) {
				SwapElements(i, leftCIdx);
				HeapifyOne(leftCIdx);	
			}
		}
		if (rightCIdx < elements.size()) {
			int cmp = comparer.apply(elements.get(i), elements.get(rightCIdx));
			if (ht == HeapType.Min && cmp > 0 ||
				ht == HeapType.Max && cmp < 0) {
				SwapElements(i, rightCIdx);
				HeapifyOne(rightCIdx);	
			}
		}
	}
	
	public void Heapify() {
		for (int i = elements.size()/2; i >= 0; i--) {
			HeapifyOne(i);
		}
	}
	
	public  T ExtractTop() {
		if (elements.size() == 0)
			return null;
		else {
			// Move the top to the end and heapify the rest
			SwapElements(0, elements.size() - 1);
			T ret = elements.remove(elements.size() - 1);
			Heapify();
			return ret;
		}
	}

	public  T PeekTop() {
		return elements.size() == 0 ? null : elements.get(0);
	}
	
	public void Insert(T elem) {
		elements.add(elem);
		Heapify();
	}
	
	public String toString() {
		return elements.toString();
	}
	
	public int size() {
		return elements.size();
	}

	public Collection<T> getElements() {
		return elements;
	}
	
	public static void main(String[] args) {
		if (true) {
			Random rand = new Random();
			HeapOnList<Integer> minHeap = new HeapOnList<>(HeapType.Min, 20, (a,b) -> a.compareTo(b));
			Integer[] data = { 10,18,9,11,10,12,18,10,8,18,18,5,8,10,12,6,17,5,0,5 };
			for (int i = 0; i < data.length; i++) {
				Integer key = rand.nextInt(20);
				System.out.printf("%s, ", key);
				minHeap.Insert(key);
			}
			System.out.println();
			System.out.printf("Heap: %s\n", minHeap);
			
			for (int i = 0; i < data.length; i++) {
				Integer top = minHeap.ExtractTop();
				System.out.printf("%s, ", top);
			}
			System.out.println();
		}
	}
}
