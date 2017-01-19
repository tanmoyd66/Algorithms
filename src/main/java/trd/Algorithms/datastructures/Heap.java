package trd.Algorithms.datastructures;

import java.util.Collection;

public class Heap<T extends Comparable<T>> {
	
	// Modes of a heap - min or max
	public static enum Mode { Min, Max };
	
	Collection<T>	storage;
	Mode			mode;
	public Heap(Collection<T> storage, Mode mode) {
		this.storage = storage; this.mode = mode;
	}
	
	// 
}
