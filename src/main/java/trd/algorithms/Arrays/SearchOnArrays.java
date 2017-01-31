package trd.algorithms.Arrays;

import trd.algorithms.utilities.ArrayPrint;
import trd.algorithms.utilities.Tuples;
import trd.algorithms.utilities.Swapper.SwapperImpl;

public class SearchOnArrays {

	// Reverse an array 
	public static <S extends Comparable<S>> void rev(S[] A, int start, int end) {
		SwapperImpl<S> swapper = new SwapperImpl<S>();
		for (int i = start, j = end; i < j; i++, j--) {
			swapper.swap(A, i, j);
		}
	}

	// Binary Search on Array
	private static <T extends Comparable<T>> Tuples.Pair<Integer, Integer> BinarySearch(T[] A, T key, int start, int end) {
		int idx = -1, loopCount = 0;
		
		while (start <= end) {
			loopCount++;
			int mid  = (start + end)/2;
			int icmp = key.compareTo(A[mid]);
			if (icmp == 0) {
				idx = mid;
				break;
			} else if (icmp < 0) {
				end = mid - 1;
			} else {
				start = mid + 1;
			}
		}
		return new Tuples.Pair<Integer, Integer>(idx, loopCount);
	}
	public static <T extends Comparable<T>> void BinarySearch(T[] A, T key) {
		Tuples.Pair<Integer, Integer> retBS = BinarySearch(A, key, 0, A.length - 1);
		System.out.printf("Index in %s of key:%s is %d (found in %d tries)\n", ArrayPrint.ArrayToString("", A), key, retBS.elem1, retBS.elem2);
	}
	
	// Rotate Array left by one
	public static <T extends Comparable<T>> void Rotate_By1(T[] A, int k) {
		// strategy is to rotate the array k times by 1
		for (int i = 0; i < k; i++) {
			T t = A[0];
			for (int j = 1; j < A.length; j++) {
				A[j - 1] = A[j];
			}
			A[A.length - 1] = t;
		}
		System.out.printf("Array %s rotated -X-1 %d times is %s\n", ArrayPrint.ArrayToString("", A), k, ArrayPrint.ArrayToString("", A));
	}

	// Rotate Array in two steps
	public static <T extends Comparable<T>> void Rotate(T[] A, int _k) {
		// Strategy:
		//		Break the array into 2 pieces: 	(0,1, ...,k-2,k-1)(k,k+1...,n-1,n)
		//		Reverse each piece:            	(k-1,k-2, ...,1,0)(n,n-1...,k+1,k)
		//		Reverse the whole array:	   	(k,k+1,... ,n-1,n)(0,1,...k-2,k-1)     
		int k = _k % A.length;
		rev(A, 0, k - 1);
		rev(A, k, A.length - 1);
		rev(A, 0, A.length - 1);
		System.out.printf("Array %s rotated once %d times is %s\n", ArrayPrint.ArrayToString("", A), k, ArrayPrint.ArrayToString("", A));
	}

	// Find number of rotations on an array
	public static <T extends Comparable<T>> int NumRotations(T[] A) {
		int index = 0;
		int start = 0, end = A.length - 1, mid = 0;
		
		while (start <= end) {
			mid = (start + end)/2;
			
			// Stopping condition is that A[mid - 1] > A[mid]
			int next = (mid + 1) % A.length;
			int prev = (mid + A.length - 1) % A.length;
			if (A[prev].compareTo(A[mid]) > 0 &&
				A[next].compareTo(A[mid]) > 0) {
				index = A.length - mid; break;
			}
			
			// check if left is sorted. if so, recurse right
			if (A[start].compareTo(A[mid]) <= 0) {
				start = mid + 1; continue;
			}
		
			// check if right is sorted. if so, recurse left
			if (A[mid].compareTo(A[end]) >= 0) {
				end = mid - 1; continue;
			}
		}
		
		System.out.printf("Array %s is rotated %d times\n", ArrayPrint.ArrayToString("", A), index);
		return index;
	}
	
	// Binary Search on rotated array by first finding the rotation point
	public static <T extends Comparable<T>> int BinarySearchOnRotatedArray1(T[] A, T key) {
		int idx   = 0;
		int iRot  = NumRotations(A);
		Tuples.Pair<Integer, Integer> retBS;

		// check if it is right or left of the pivot point
		if (iRot != 0) {
			if (A[0].compareTo(key) > 0 && A[iRot - 1].compareTo(key) < 0) {
				retBS = BinarySearch(A, key, 0, iRot - 1);
			} else {
				retBS = BinarySearch(A, key, iRot, A.length);
			}
		} else {
			retBS = BinarySearch(A, key, 0, A.length - 1);
		}
		
		System.out.printf("Index in %s of key:%s is %d (found in %d tries)\n", ArrayPrint.ArrayToString("", A), key, retBS.elem1, retBS.elem2);
		return idx;
	}
	
	// Binary Search on rotated array - collapsed rotation point finding and searching
	// Strategy:
	// 		Find mid
	//		If key is present at mid, return mid.
	//		Else If A[start..mid] is sorted
	//			If key to be searched lies in range from A[start]..A[mid]
	//		      	Recurse for key between start..mid
	//			Else recur for A[mid+1..end]
	//		Else (A[mid+1..end] must be sorted)
	//			If key to be searched lies in range from A[mid+1]..A[end]
	//				recur for A[mid+1..end].
	//			Else recur for A[start..mid] 
	public static <T extends Comparable<T>> int BinarySearchOnRotatedArray2(T[] A, T key) {
		int idx   = 0;
		int start = 0, end = A.length - 1, mid = 0;
		int loopCount = 0;
		
		while (start <= end) {
			loopCount++;
			
			// Find mid of current frame. Return if key is at that point
			mid = (start + end)/2;
			if (A[mid].compareTo(key) == 0) {
				idx = mid; break;
			} else {
				// Check if start..mid is a sorted segment
				if (A[start].compareTo(A[mid]) <= 0) {
					// Do binary search in this segment
					if (A[mid].compareTo(key) > 0) {
						end = mid - 1;
					} else {
						start = mid + 1;
					}
				} else {
					// start..mid is not a sorted segment (which in turn means that mid+1..end is sorted)
					if (A[mid].compareTo(key) > 0) {
						start = mid + 1;
					} else {
						// must be in the start..mid side
						end = mid;
					}
				}
			}
		}
		
		System.out.printf("Index in %s of key:%s is %d (found in %d tries)\n", ArrayPrint.ArrayToString("", A), key, idx, loopCount);
		return idx;
	}
	
	public static void main(String[] args) {
		if (true) {
			Integer[] A = new Integer[] {-11, -10, -8, 8, 6, 11, 12, 23};
			BinarySearch(A, -8);
			BinarySearch(A, -10);
			BinarySearch(A, 23);
		}
		
		if (true) {
			Rotate_By1(new Integer[] {-11, -10, -8, 8, 6, 11, 12, 23}, 2);
			Rotate_By1(new Integer[] {-11, -10, -8, 8, 6, 11, 12, 23}, 4);
		}
		
		if (true) {
			Rotate(new Integer[] {-11, -10, -8, 8, 6, 11, 12, 23}, 2);
			Rotate(new Integer[] {-11, -10, -8, 8, 6, 11, 12, 23}, 4);
		}
		
		if (true) {
			Integer[] AA = new Integer[] {-11, -10, -8, 6, 8, 11, 12, 23};
			Rotate(AA, 1);
			int nRot = NumRotations(AA);
			BinarySearchOnRotatedArray1(AA, 23);
			BinarySearchOnRotatedArray1(AA, -8);
			BinarySearchOnRotatedArray1(AA, -10);
			BinarySearchOnRotatedArray2(AA, 23);
			BinarySearchOnRotatedArray2(AA, -8);
			BinarySearchOnRotatedArray2(AA, -10);
		}
	}

}
