package trd.Algorithms.Arrays;

import trd.Algorithms.utilities.Tuples;

public class ArrayProblems {
	
	public static interface ISwapper<T extends Comparable<T>> {
		void swap(T[] target, int i, int j);
		boolean less(T v, T w);
		int compare(T v, T w);
	}
	
	public static class Swapper<T extends Comparable<T>> implements ISwapper<T> {
		public int SwapCount = 0;
		public int CompareCount = 0;
		
		public void swap(T[] A, int i, int j) {
			T temp; temp = A[i]; A[i] = A[j]; A[j] = temp;
			++SwapCount;
		}
		
	    // is v < w ?
		public boolean less(T v, T w) {
			CompareCount++;
			return v.compareTo(w) < 0;
	    }

		// is v < w ?
		public int compare(T v, T w) {
			CompareCount++;
			return v.compareTo(w);
	    }
	}
	
	
	public ArrayProblems() {
	}

	public static <S> String ArrayToString(String tag, S[] A) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s:[", tag));
		for (int i = 0; i < A.length; i++)
			sb.append(String.format("%s ", A[i]));
		sb.append(String.format("]", tag));
		return sb.toString();
	}

	public static <S> String MatrixToString(S[][] A, int M, int N) {
		StringBuilder sb = new StringBuilder();
		sb.append("----------------------------\n");
		for (int row = 0; row < M; row++) {
			for (int col = 0; col < N; col++) {
				sb.append(String.format("%4s ", A[row][col]));
			}
			sb.append("\n");
		}
		sb.append("----------------------------\n");
		return sb.toString();
	}

	// Find the minimum in an array
	public static <T extends Comparable<T>> int min(T[] A, int start, int end) {
		int lastMin = start;
		for (int i = start; i < end; i++) {
			int icmp = A[lastMin].compareTo(A[i]);
			lastMin = icmp > 0 ? i : lastMin;
		}
		return lastMin;
	}

	// Find the maximum in an array
	public static <T extends Comparable<T>> int max(T[] A, int start, int end) {
		int lastMax = start;
		for (int i = start; i < end; i++) {
			int icmp = A[lastMax].compareTo(A[i]);
			lastMax = icmp < 0 ? i : lastMax;
		}
		return lastMax;
	}
	
	// Reverse an array 
	public static <S extends Comparable<S>> void rev(S[] A, int start, int end) {
		Swapper<S> swapper = new Swapper<S>();
		for (int i = start, j = end; i < j; i++, j--) {
			swapper.swap(A, i, j);
		}
	}
	
	// There is a set of 0, 1 and 2s. Sort them in order.
	public static void DutchNationalFlag(Integer[] A) {

		Swapper<Integer> swapper = new Swapper<Integer>();

		// Track the end positions of 0s and 1s and the start position of 2s
		// We actually know that e1 = i - 1 (use that as invariant to not having to track e1)
		int e0 = -1, s2 = A.length;
		
		// i is your running variable
		int i = 0;
		
		// Forward until you reach the first 0
		while (A[i] != 0) i++;
		swapper.swap(A, ++e0, i); i = 1;
		
		// Grow the unknown region until i is greater than s2 (start of 2s)
		while (i < s2) {
			if (A[i] == 0) {
				// If i points to a 0, we swap it to the end of the 0s and move i forward.
				swapper.swap(A, ++e0, i++);
			} else if (A[i] == 1) {
				// If i points to a 1, we just move forward.
				i++;
			} else {
				// This is tricky. You do not want to move forward i since you do not know 
				//    if i is pointing to a 0. In that case we need another swap to position that right.
				swapper.swap(A, i, --s2);
			}
		}
		
		System.out.printf("%s\n", ArrayToString("Dutch National Flag:", A));
	}
	
	// Given an array of numbers find the maximal difference
	// argmax [i<j] (A[j] - A[i])
	// O(n^2) 
	public static void FindMaxDifference_BruteForce(Integer[] A) {
		Tuples.Triple<Integer, Integer, Integer> ret = new Tuples.Triple<Integer, Integer, Integer>(Integer.MIN_VALUE, 0, 0);
		for (int i = 0; i < A.length - 1; i++) {
			for (int j = i + 1; j < A.length; j++) {
				int thisDiff = A[j] - A[i];
				if (thisDiff > ret.e1) {
					ret.e1 = thisDiff;
					ret.e2 = i; ret.e3 = j;
				}
			}
		}
		System.out.printf("%s: %d:[%d-%d]\n", ArrayToString("Max Diff:", A), ret.e1, ret.e2, ret.e3);
	}

	public static void FindMaxDifference_DQ(Integer[] A, int start, int end, Tuples.Triple<Integer, Integer, Integer> curr) {
		
		if (start == 4 && end == 7)
			{ int i = 0; }
		
		if (end - start == 1) {
			if (curr.e1 < (A[end] - A[start])) {
				curr.e1 = curr.e1 < (A[end] - A[start]) ? A[end] - A[start] : curr.e1;
				curr.e2 = start;
				curr.e3 = end;
				return;
			} else {
				return;
			}
		} else {
			
			// Divide the range (start, end) into two pieces and do a recursive find in each piece
			int mid = (start + end)/2; 
			if (end/2 > start)
				FindMaxDifference_DQ(A, start, mid , curr);
			Tuples.Triple<Integer, Integer, Integer> retL = new Tuples.Triple<Integer, Integer, Integer>(curr.e1, curr.e2, curr.e3);
			if (end > (end/2 + 1))
				FindMaxDifference_DQ(A, mid + 1, end, curr);
			Tuples.Triple<Integer, Integer, Integer> retR = new Tuples.Triple<Integer, Integer, Integer>(curr.e1, curr.e2, curr.e3);
			
			// O(n) merge between the two halves where start is in left and end is in right
			int minLI = min(A, start, mid);
			int minRI = max(A, mid + 1, end - 1);
			int diff  = A[minRI] - A[minLI];
			
			// Find which one is the maximal
			Integer[] theThree = new Integer[] { diff, retL.e1, retR.e1 };
			int whichIsMax = max(theThree, 0, theThree.length);
			
			switch (whichIsMax) {
				case 0: curr = new Tuples.Triple<Integer, Integer, Integer>(diff, minLI, minRI); return;
				case 1: curr = retL; return;
				case 2: curr = retR; return;
			}
		}
	}	
	public static void FindMaxDifference_DivideAndConquer(Integer[] A) {		
		Tuples.Triple<Integer, Integer, Integer> ret = new Tuples.Triple<Integer, Integer, Integer>(Integer.MIN_VALUE, 0, 0); 
		FindMaxDifference_DQ(A, 0, A.length - 1, ret);
		System.out.printf("%s: %d:[%d-%d]\n", ArrayToString("Max Diff DQ:", A), ret.e1, ret.e2, ret.e3);
	}
	
	// Maximal sum sub-array
	public static void MaximalSumSubArray(Integer[] A) {		
		Tuples.Triple<Integer, Integer, Integer> last = new Tuples.Triple<Integer, Integer, Integer>(Integer.MIN_VALUE, 0, 0); 
		int start = Integer.MIN_VALUE, end = 0, sum = 0;
		while (end < A.length) {
			if (A[end] < 0) {
				if (sum > last.e1) {
					last.e1 = sum; last.e2 = start; last.e3 = end - 1;
				}
				sum = 0;
				end++;
				start = end; 
			} else {
				sum += A[end++];
			}
		}
		if (sum > last.e1) {
			last.e1 = sum; last.e2 = start; last.e3 = end - 1;
		}
		System.out.printf("%s: %d:[%d-%d]\n", ArrayToString("Max Sum Sub Array:", A), last.e1, last.e2, last.e3);
	}

	// Maximal sum sub-array
	public static void LongestIncreasingSubArray(Integer[] A) {		
		Tuples.Triple<Integer, Integer, Integer> last = new Tuples.Triple<Integer, Integer, Integer>(Integer.MIN_VALUE, 0, 0); 
		int start = 0, end = 1, currLength = 0;
		while (end < A.length) {
			if (A[end] < A[end - 1]) {
				if (last.e1 < currLength ) {
					last.e2 = start; last.e3 = end - 1; last.e1 = currLength;
				}
				currLength = 1; start = end++;
			} else {
				end++; 
				currLength++;
			}
		}
		
		if ((start < (end - 1)) && (currLength > last.e1)) {
			last.e2 = start; last.e3 = end - 1;
		}
		System.out.printf("%s: %d:[%d-%d]\n", ArrayToString("Longest Increasing Sub Array:", A), last.e1, last.e2, last.e3);
	}

	
	// Reverse words in a sentence
	public static void ReverseWord(String _sentence) {
		char[] __sentence = _sentence.toCharArray();
		Character[] sentence = new Character[__sentence.length];
		for (int i = 0; i < sentence.length; i++)
			sentence[i] = __sentence[i];
		rev(sentence, 0, sentence.length - 1);
		for (int start = 0, end = 0; end < sentence.length; end++) {
			if (sentence[end] == ' ') {
				if (end > start) {
					rev(sentence, start, end - 1);
					start = end + 1;
				}
			}
		}
		for (int i = 0; i < sentence.length; i++)
			__sentence[i] = sentence[i];
		System.out.printf("Reversed Words of [%s]: [%s]\n", _sentence, new String(__sentence));
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
		System.out.printf("Index in %s of key:%s is %d (found in %d tries)\n", ArrayToString("", A), key, retBS.elem1, retBS.elem2);
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
		System.out.printf("Array %s rotated -X-1 %d times is %s\n", ArrayToString("", A), k, ArrayToString("", A));
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
		System.out.printf("Array %s rotated once %d times is %s\n", ArrayToString("", A), k, ArrayToString("", A));
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
		
		System.out.printf("Array %s is rotated %d times\n", ArrayToString("", A), index);
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
		
		System.out.printf("Index in %s of key:%s is %d (found in %d tries)\n", ArrayToString("", A), key, retBS.elem1, retBS.elem2);
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
		
		System.out.printf("Index in %s of key:%s is %d (found in %d tries)\n", ArrayToString("", A), key, idx, loopCount);
		return idx;
	}
	
	// Two dimensional Binary Search
	// A[M,N] is a matrix with the property that: All columns are sorted in ascending order top to bottom and all rows left to right
	// Given A[M,N] find an element in the matrix
	// Strategy:
	//		if key is less    than A[i,j] then we can prune all columns greater than j and move to a lower column
	//		if key is greater than A[i,j] then we can prune all rows less than i and move to a higher row 
	public static <T extends Comparable<T>> Tuples.Pair<Integer, Integer> FindElementIn2DMatrix(T[][] A, T key, int M, int N) {
		int 	row = 0, col = N - 1;
		boolean found = false;
		while (row < M && col >= 0) {
			int cmp = key.compareTo(A[row][col]);			
			if (cmp == 0) {
				found = true;
				break;
			} else if (cmp < 0) {
				col--;
			} else {
				row++;
			}
		}
		System.out.printf("Index in \n%s of key:%s is %s\n", MatrixToString(A, M, N), key, found ? String.format("[%d,%d]", row, col) : "not found");		
		return found ? new Tuples.Pair<Integer, Integer>(row, col) : null;
	}
	
	public static void main(String[] args) {
		DutchNationalFlag(new Integer[] {1, 0, 2});
		DutchNationalFlag(new Integer[] {0, 1, 2});
		DutchNationalFlag(new Integer[] {1, 2, 0});
		DutchNationalFlag(new Integer[] {1, 0, 1, 1, 2, 1, 2, 0});
		
		FindMaxDifference_BruteForce(new Integer[] {-10, 11, -8, 8, 6, 12, -11, 30});
		FindMaxDifference_DivideAndConquer(new Integer[] {-10, 11, -8, 8, 6, 12, -11, 30});
		MaximalSumSubArray(new Integer[] {-10, 11, -8, 8, 6, 12, -11, 23});
		LongestIncreasingSubArray(new Integer[] {-10, 11, -8, 8, 6, 12, -11, 23});

		Integer[] A = new Integer[] {-11, -10, -8, 8, 6, 11, 12, 23};
		BinarySearch(A, -8);
		BinarySearch(A, -10);
		BinarySearch(A, 23);

		Rotate_By1(new Integer[] {-11, -10, -8, 8, 6, 11, 12, 23}, 2);
		Rotate_By1(new Integer[] {-11, -10, -8, 8, 6, 11, 12, 23}, 4);

		Rotate(new Integer[] {-11, -10, -8, 8, 6, 11, 12, 23}, 2);
		Rotate(new Integer[] {-11, -10, -8, 8, 6, 11, 12, 23}, 4);

		Integer[] AA = new Integer[] {-11, -10, -8, 6, 8, 11, 12, 23};
		Rotate(AA, 1);
		int nRot = NumRotations(AA);
		BinarySearchOnRotatedArray1(AA, 23);
		BinarySearchOnRotatedArray1(AA, -8);
		BinarySearchOnRotatedArray1(AA, -10);
		BinarySearchOnRotatedArray2(AA, 23);
		BinarySearchOnRotatedArray2(AA, -8);
		BinarySearchOnRotatedArray2(AA, -10);

		FindElementIn2DMatrix(new Integer[][] {{10, 20, 30, 40}, {50, 60, 70, 80}, {90, 100, 110, 120}, {130, 140, 150, 160}}, 10 , 4, 4);
		ReverseWord("I am a bad boy");
	}
}
