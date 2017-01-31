package trd.algorithms.Arrays;

import java.util.List;

import trd.algorithms.utilities.ArrayPrint;
import trd.algorithms.utilities.Swapper.SwapperImpl;
import trd.algorithms.utilities.Tuples;

public class PotpourriOnArrays {
		
	public PotpourriOnArrays() {
	}

	// Reverse an array 
	public static <S extends Comparable<S>> void rev(S[] A, int start, int end) {
		SwapperImpl<S> swapper = new SwapperImpl<S>();
		for (int i = start, j = end; i < j; i++, j--) {
			swapper.swap(A, i, j);
		}
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
		System.out.printf("Index in \n%s of key:%s is %s\n", 
						ArrayPrint.MatrixToString(A, M, N), key, found ? String.format("[%d,%d]", row, col) : "not found");		
		return found ? new Tuples.Pair<Integer, Integer>(row, col) : null;
	}


	// Given an array of towers find the water collected between them.
	// For water to collect between 3 towers X, Y, Z we need to have X > Y and Y < Z
	//		Here Y can be broken down into Y1, Y2, Y3, ... Yk where for all i, X > Yi and Yi < Z 
	public static int WaterCollectedBetweenTowers(Integer[] A) {
	    int maxHeight = 0;
	    int previousHeight = 0;
	    int previousHeightIndex = 0;
	    int coll = 0;
	    int temp = 0;

	    // find the first peak (all water before will not be collected)
	    while (A[previousHeightIndex] > maxHeight) {
	        maxHeight = A[previousHeightIndex];
	        previousHeightIndex++;
	        if (previousHeightIndex == A.length)            // in case of stairs (no water collected)
	            return coll;
	        else
	            previousHeight = A[previousHeightIndex];
	    }

	    for (int i = previousHeightIndex; i < A.length; i++) {
	        if (A[i] >= maxHeight) {      // collect all temp water
	            coll += temp;
	            temp = 0;
	            maxHeight = A[i];        // new max height
	        }
	        else {
	            temp += maxHeight - A[i];
	            if(A[i] > previousHeight) {  // we went up... collect some water
	                int collWater = (i-previousHeightIndex)*(A[i]-previousHeight);
	                coll += collWater;
	                temp -= collWater;
	            }
	        }

	        // previousHeight only changes if consecutive towers are not same height
	        if(A[i] != previousHeight) {
	            previousHeight = A[i];
	            previousHeightIndex = i;
	        }
	    }
	    return coll;
	}
	
	// Given an unsorted array, find the maximal gap between successive elements
	public static Tuples.Pair<Integer, Integer> MaximumGap(Integer[] A) {
		int gap = 0, maxGapAt = 0;
		for (int i = 1; i < A.length; i++) {
			int thisGap = Math.abs(A[i] - A[i - 1]);
			if (thisGap > gap) {
				gap = thisGap; maxGapAt = i - 1;
			}
		}
		return new Tuples.Pair<Integer, Integer>(gap, maxGapAt);
	}
	
	// Given an array of parenthesis, find the longest string of valid parenthesis
	// We will do it in O(n) (two scans) with no extra space
	// We will keep 2 pointers: left and right
	public static int LongestStringOfValidParentheses(String s) {
		
		// left tracks the number of left parenthesis seen and right the number of right parenthesis seen
		// max-length is the maximal length of valid parenthesis seen
		int left = 0, right = 0, maxlength = 0;
		
		// Scan from left to right 
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '(') {
				left++;
			} else {
				right++;
			}
			
			if (left == right) {
				// We are good till this point and we have seen x left and rights
				// So the valid length is 2x
				maxlength = Math.max(maxlength, 2 * right);
			} else if (right >= left) {
				// We have seen more rights than left
				// Abandon current run and restart
				left = right = 0;
			}
		}
		
		left = right = 0;

		// Scan from right to left
		for (int i = s.length() - 1; i >= 0; i--) {
			if (s.charAt(i) == '(') {
				left++;
			} else {
				right++;
			}

			if (left == right) {
				// We are good till this point and we have seen x left and rights
				// So the valid length is 2x
				maxlength = Math.max(maxlength, 2 * left);
			} else if (left >= right) {
				// We have seen more rights than left
				// Abandon current run and restart
				left = right = 0;
			}
		}
		return maxlength;
	}
	
	// Reverse an integer
	public static int ReverseInteger(int num) {
		int ret = 0;
		while (num != 0) {
			ret = ret * 10 + num % 10;
			num /= 10;
		}
		return ret;
	}

	// Check if an integer is palindromic
	public static boolean CheckIfPalindromic(int num) {
		return num == ReverseInteger(num);
	}

	// Print a 2 dimensional matrix is spiral order
	public static List<Integer> SprialPrint(Integer[][] A) {
		int retVal = 0;
		while (true) {
			
		}
	}
	public static void main(String[] args) {
		if (true) {
			FindElementIn2DMatrix(new Integer[][] {{10, 20, 30, 40}, {50, 60, 70, 80}, {90, 100, 110, 120}, {130, 140, 150, 160}}, 10 , 4, 4);
			ReverseWord("I am a bad boy");
		}
		
		if (true) {
			Integer[] towers = new Integer[] { 7,1,5,2,4,1,6,9,1,2 };
			System.out.printf("Water collected between towers: %d %s]\n", 
								WaterCollectedBetweenTowers(towers), ArrayPrint.ArrayToString("", towers));
		}
		if (true) {
			Integer[] A = new Integer[] { 7,1,5,2,4,1,6,9,1,2 };
			Tuples.Pair<Integer, Integer> ret = MaximumGap(A);
			System.out.printf("Maximal Gap in %s is %d at %d]\n", ArrayPrint.ArrayToString("", A), ret.elem1, ret.elem2);
		}
		
		if (true) {
			String string = "(((()()())()()))";
			System.out.printf("Longest String of valid parenthesis in %s is %d\n", string, LongestStringOfValidParentheses(string));
		}

		if (true) {
			int i = 12321;
			System.out.printf("Reverse of Integer %d is %d and it is %sly palindromic\n", i, ReverseInteger(i), CheckIfPalindromic(i) ? "tru" : "false");
		}
	}
}
