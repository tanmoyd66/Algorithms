package trd.algorithms.strings;

import java.util.Arrays;
import java.util.List;

import trd.algorithms.utilities.Swapper;
import trd.algorithms.utilities.Swapper.ISwapper;

public class StringSortingAlgorithms {
	
	private static final int radix 		= 256;
	private static final int smallArray = 15; 
	
	public static void LeastSignificantDigitSort(String[] A, int maxDigits) { 

		// Sort a[] on leading W characters.
		int size  = A.length;
		String[] aux = new String[size];
	
		// Repeat for each of the maxDigits from right to left
		for (int d = maxDigits-1; d >= 0; d--) {
			// For each digit:
			
			// Compute frequency counts.
			int[] count = new int[radix + 1]; 
			for (int i = 0; i < size; i++)
				count[A[i].charAt(d) + 1]++;
	
			// Transform counts to indices.
			for (int r = 0; r < radix; r++) 
				count[r+1] += count[r];
	
			// Distribute, using auxiliary array
			for (int i = 0; i < size; i++) 
				aux[count[A[i].charAt(d)]++] = A[i];
			
			// Copy back.
			for (int i = 0; i < size; i++) 
				A[i] = aux[i];
		}
	}


	public static void InsertionSort(String[] A, ISwapper<String> swapper, int lo, int hi, int d) { 
		// Sort from a[lo] to a[hi], starting at the dth character.
		for (int i = lo; i <= hi; i++)
			for (int j = i; j > lo && less(A[j], A[j-1], d); j--)
				swapper.swap(A, j, j-1);
	}
	
	private static boolean less(String v, String w, int d) { 
		return v.substring(d).compareTo(w.substring(d)) < 0; 
	}
	
	private static int charAt(String s, int d) { 
		if (d < s.length()) return s.charAt(d); else return -1; 
	}
	
	private static void MSDSort(String[] A, ISwapper<String> swapper, String[] aux, int lo, int hi, int d) { 
	
		// For small arrays do insertion sort
		if (hi <= lo + smallArray) { 
			InsertionSort(A, swapper, lo, hi, d); 
			return; 
		}
		
		// radix + 1 is reserved for those strings that are shorter than d
		
		// Compute frequency counts.
		int[] count = new int[radix + 2]; 
		for (int i = lo; i <= hi; i++)
			count[charAt(A[i], d) + 2]++;

		// Transform counts to indices.
		for (int r = 0; r < radix + 1; r++) 
			count[r + 1] += count[r];
		
		// Distribute to Auxiliary Array
		for (int i = lo; i <= hi; i++) // Distribute.
			aux[count[charAt(A[i], d) + 1]++] = A[i];
		for (int i = lo; i <= hi; i++) // Copy back.
			A[i] = aux[i - lo];
		
		// Recursively sort for each character value.
		for (int r = 0; r < radix; r++)
			MSDSort(A, swapper, aux, lo + count[r], lo + count[r+1] - 1, d+1);
	}
	
	public static void MostSignificantDigitSort(String[] A) {
		String[] aux = new String[A.length];
		Swapper.SwapperImpl<String> swapper = new Swapper.SwapperImpl<>();
		MSDSort(A, swapper, aux, 0, A.length - 1, 0);
	}

	// Approach is to partition into 3 sub arrays and recursively sort them
	// 		(1) A[lo..lt-1] is an array that has the d-th character smaller than (2)
	//		(2) A[lt..gt]   is an array that has the d-th character identical
	//		(3) A[gt+1..hi] is an array that has the d-th character greater than (2)
	private static void ThreeWayStringQuickSort(String[] A, ISwapper<String> swapper, int lo, int hi, int d) {
		if (hi <= lo) 
			return;
	
		int lt = lo, gt = hi;
		int v = charAt(A[lo], d);
		int i = lo + 1;
		
		while (i <= gt) {
			int t = charAt(A[i], d);
			if (t < v) 
				swapper.swap(A, lt++, i++);
			else if (t > v) 
				swapper.swap(A, i, gt--);
			else i++;
		}
	
		// A[lo..lt-1]
		ThreeWayStringQuickSort(A, swapper, lo, lt - 1, d);
		
		// A[lt..gt]
		if (v >= 0) {
			ThreeWayStringQuickSort(A, swapper, lt, gt, d + 1);
		}

		// A[gt+1..hi]
		ThreeWayStringQuickSort(A, swapper, gt + 1, hi, d);
	}
	
	public static void ThreeWayStringQuickSort(String[] a) { 
		Swapper.SwapperImpl<String> swapper = new Swapper.SwapperImpl<>();
		ThreeWayStringQuickSort(a, swapper, 0, a.length - 1, 0); 
	}

	public static void main(String[] args) {
		if (true) {
			String[] keys = new String[] {"4PGC938", "2IYE230", "3CIO720", "1ICK750", "1OHV845", "4JZY524", "1ICK750", 
											"3CIO720", "1OHV845", "1OHV845", "2RLA629", "2RLA629", "3ATW723", };
			List<String> input = Arrays.asList(keys);
			System.out.printf("Original  : %s\n", input);
			LeastSignificantDigitSort(keys, 7);
			System.out.printf("LSD Sorted: %s\n", input);
		}

		if (true) {
			String[] keys = new String[] {"4PGC938", "2IYE230", "3CIO720", "1ICK750", "1OHV845", "4JZY524", "1ICK750", 
					"3CIO720", "1OHV845", "1OHV845", "2RLA629", "2RLA629", "3ATW723", };
			List<String> input = Arrays.asList(keys);
			MostSignificantDigitSort(keys);
			System.out.printf("MSD Sorted: %s\n", input);
		}

		if (true) {
			String[] keys = new String[] {"4PGC938", "2IYE230", "3CIO720", "1ICK750", "1OHV845", "4JZY524", "1ICK750", 
					"3CIO720", "1OHV845", "1OHV845", "2RLA629", "2RLA629", "3ATW723", };
			List<String> input = Arrays.asList(keys);
			ThreeWayStringQuickSort(keys);
			System.out.printf("3QS Sorted: %s\n", input);
		}
	}
}
