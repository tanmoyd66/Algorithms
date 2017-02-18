package trd.algorithms.Arrays;

import java.util.Iterator;
import java.util.NoSuchElementException;

import trd.algorithms.utilities.ArrayPrint;
import trd.algorithms.utilities.Swapper;
import trd.algorithms.utilities.Swapper.ISwapper;

public class PermutationIterator<T extends Comparable<T>> implements Iterator<T[]>{
	
	//-----------------------------------------------------------------------------------------------------
	// All permutations of an Array: Recursive Formulation
	//-----------------------------------------------------------------------------------------------------
	private T[] 		nextPermutation;
	private final T[] 	allElements;
	private Integer[] 	indices;
	ISwapper<Integer>	swapper;

	public PermutationIterator(T[] allElements) {
		this.allElements = allElements;
		this.indices = new Integer[allElements.length];
		this.swapper = new Swapper.SwapperImpl<Integer>();
		
		if (allElements.length == 0) {
			nextPermutation = null;
			return;
		}

		// Initialize with (0, 1, 2, ..., n)
		for (int i = 0; i < indices.length; ++i) {
			indices[i] = i;
		}
		LoadPermutation();
	}

	@Override
	public boolean hasNext() {
		return nextPermutation != null;
	}

	@Override
	public T[] next() {
		if (nextPermutation == null)
			throw new NoSuchElementException("Out of range");

		T[] ret = nextPermutation;
		GenerateNextPermutation();
		return ret;
	}

	// Strategy:
	//	 Generate by lexicographic ordering
	//	 We will divide the number into 2 pieces
	//   [Ascending][Descending]
	//	 
	private void GenerateNextPermutation() {
		
		// Position decrease to the rightmost element in the ascending part
		int decrease = indices.length - 2;
		while (decrease >= 0 && indices[decrease] > indices[decrease + 1]) {
			--decrease;
		}

		// No more new permutations.
		if (decrease == -1) {
			nextPermutation = null;
			return;
		}

		// Start from the right and find the largest number greater than decrease
		int larger; 
		for (larger = indices.length - 1; indices[larger] < indices[decrease]; larger--);
				
		// Swap larger and decrease 
		swapper.swap(indices, decrease, larger);
		
		// Reverse the Descending part
		int j = indices.length - 1; int i = decrease + 1;
		while (i < j) {
			swapper.swap(indices, i++, j--);
		}

		LoadPermutation();
	}

	// Creates the permutation given the index array
	@SuppressWarnings("unchecked")
	private void LoadPermutation() {
		Comparable<T>[] thisPerm = new Comparable[allElements.length];
		for (int i = 0; i < indices.length; i++)
			thisPerm[i] = allElements[indices[i]];
		this.nextPermutation = (T[]) thisPerm;
	}

    public static void main(String[] args) {
    	Integer[] A = new Integer[] { 1, 2, 3, 4, 5 }; int i = 0;
    	PermutationIterator<Integer> pi = new PermutationIterator<Integer>(A);
    	while (pi.hasNext()) {
    		Comparable<Integer>[] thisPerm = pi.next();
    		System.out.printf("%s\n", ArrayPrint.ArrayToString(String.format("%3d",++i), thisPerm));
    	}
    }
}
