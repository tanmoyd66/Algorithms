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

	private void GenerateNextPermutation() {
		
		int i = indices.length - 2;

		while (i >= 0 && indices[i] > indices[i + 1]) {
			--i;
		}

		// No more new permutations.
		if (i == -1) {
			nextPermutation = null;
			return;
		}

		int j = i + 1;
		int min = indices[j];
		int minIndex = j;

		while (j < indices.length) {
			if (indices[i] < indices[j] && indices[j] < min) {
				min = indices[j];
				minIndex = j;
			}

			++j;
		}

		swapper.swap(indices, i, minIndex);
		++i;
		j = indices.length - 1;

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
