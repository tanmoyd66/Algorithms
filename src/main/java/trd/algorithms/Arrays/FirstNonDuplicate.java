package trd.algorithms.Arrays;

import java.util.HashMap;
import java.util.Map;

import trd.algorithms.utilities.ArrayPrint;
import trd.algorithms.utilities.Utilities;

public class FirstNonDuplicate<T extends Comparable<T>> {
	
	// Order N^2 algorithm
	public static <T extends Comparable<T>> T FindOn2(T[] A) {
		for (int i = 0; i < A.length - 1; i++) {
			boolean duplicateFound = false;
			for (int j = 0; !duplicateFound && j < A.length; j++) {
				if (i == j)
					continue;
				if (A[i].compareTo(A[j]) == 0)
					duplicateFound = true;
			}
			if (!duplicateFound)
				return A[i];
		}
		return null;
	}
	
	// Order N algorithm using HashTable
	public static <T extends Comparable<T>> T FindOn(T[] A) {
		// Hash the first positions into a map
		// Remove from the map if a duplicate found
		HashMap<T,Integer> firstPos = new HashMap<>();
		for (int i = 0; i < A.length - 1; i++) {
			Integer fp = firstPos.get(A[i]);
			if (fp == null) {
				firstPos.put(A[i], i);
			} else {
				firstPos.remove(A[i]);
			}
		}
		
		// Iterate thru all map entries and emit the one with lowest val
		T firstNonDuplicate = null; int firstNonDuplicateIdx = A.length;
		for (Map.Entry<T, Integer> me : firstPos.entrySet()) {
			if (me.getValue() < firstNonDuplicateIdx) {
				firstNonDuplicate = me.getKey(); firstNonDuplicateIdx = me.getValue();
			}
		}
		return firstNonDuplicate;
	}

	public static void main(String[] args) {
		Integer[]  A  = new Integer[] {1, 3, 3, 1, 4, 2};
		Character[] C = Utilities.StringToCharacterArray("google");
		System.out.printf("%s: %s %s\n", ArrayPrint.ArrayToString("", A), 
								FirstNonDuplicate.FindOn2(A), FirstNonDuplicate.FindOn(A));
		System.out.printf("%s: %s %s\n", ArrayPrint.ArrayToString("", C), 
								FirstNonDuplicate.FindOn2(C), FirstNonDuplicate.FindOn(C));
	}
}
