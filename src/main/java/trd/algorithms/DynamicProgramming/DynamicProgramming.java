package trd.algorithms.DynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.google.common.primitives.Chars;

import trd.algorithms.utilities.ArrayPrint;
import trd.algorithms.utilities.CompositeKey;
import trd.algorithms.utilities.Swapper;
import trd.algorithms.utilities.Swapper.ISwapper;
import trd.algorithms.utilities.Tuples;

public class DynamicProgramming {
	
	
	//-----------------------------------------------------------------------------------------------------
	// Given a path-graph of n vertices, find the maximal sum such that:
	//	  2 consecutive vertices cannot be included in a sum
	// Strategy:
	//	  A[n] = max(A[n-1], A[n-2] + v(n))
	public static List<Integer> MaximalWeightedIndependentSet(Integer[] vertices, int end) {
		
		if (end == 0)
			return new ArrayList<Integer>(vertices[end]);
		else if (end < 0)
			return new ArrayList<Integer>();
		
		List<Integer> Last 			= MaximalWeightedIndependentSet(vertices, end - 1);
		List<Integer> LastPartOne	= MaximalWeightedIndependentSet(vertices, end - 2);
		
		LastPartOne.add(vertices[end]);
		int sumLast 		= Last.stream().reduce(0, (x,y) -> x+y);;
		int sumLastPartOne 	= LastPartOne.stream().reduce(0, (x,y) -> x+y);;
		
		return sumLast > sumLastPartOne ? Last: LastPartOne;
	}
		
	//-----------------------------------------------------------------------------------------------------
	// All permutations of a string: Recursive Formulation
    public static void Permute(Character[] ary, int startIndex, int endIndex, List<String> output, ISwapper<Character> swapper) {
        if (startIndex == endIndex){
        	output.add(String.valueOf(Tuples.CharacterArrayToCharArray(ary)));
        } else {
            for (int i = startIndex; i <= endIndex; i++) {
				swapper.swap(ary, startIndex, i);
				Permute(ary, startIndex + 1, endIndex, output, swapper);
				swapper.swap(ary, startIndex, i);
            }
        }
    }
	//-----------------------------------------------------------------------------------------------------

	//-----------------------------------------------------------------------------------------------------
    // Longest Palindromic substring
    // P[i,j]   = true if S[i..j] is a palindrome
    //		    = P[i+1,j-1] + 1 * (S[i] == S[j])
    // P[i,i]   = 1
    // P[i,i+1] = (S[i] == S[i+1]) ? 1 : 0
	public static class LongestPalindromicSubstring {
		private int lo, maxLen;

		public String getPalindrome(String s) {
			int len = s.length();
			if (len < 2)
				return s;

			for (int i = 0; i < len - 1; i++) {
				extendPalindrome(s, i, i); // assume odd length, try to extend Palindrome as possible
				extendPalindrome(s, i, i + 1); // assume even length.
			}
			return s.substring(lo, lo + maxLen);
		}

		private void extendPalindrome(String s, int j, int k) {
			while (j >= 0 && k < s.length() && s.charAt(j) == s.charAt(k)) {
				j--;
				k++;
			}
			if (maxLen < k - j - 1) {
				lo = j + 1;
				maxLen = k - j - 1;
			}
		}
	}
    
	//-----------------------------------------------------------------------------------------------------
    
	public static void main(String[] args) {
		
		if (true){
			Integer[] A = {1, 4, 5, 4};
			List<Integer> wis = MaximalWeightedIndependentSet(A, A.length - 1);
			System.out.printf("MaximalWeightedIndependentSubset of %s is:%s=%d\n", 
							  ArrayPrint.ArrayToString("", A), wis, wis.stream().reduce(0, (x,y)->(x+y)));
		}
		
		if (true) {
	        String inputString = "ABCDE";
	        List<String> output = new ArrayList<String>();
	        Character[] charArray = Lists.newLinkedList(Chars.asList(inputString.toCharArray())).toArray(new Character[0]);
	        Permute(charArray, 0, inputString.length()-1, output, new Swapper.SwapperImpl<Character>());
	        System.out.printf("Permutations of %s is: (%d) %s\n", inputString, output.size(), output); 
		}
		
		if (true) {
			String string = "abacdfgdcaba";
	        System.out.printf("Longest Palindromic Substring of %s is: (%s)\n", string, new LongestPalindromicSubstring().getPalindrome(string)); 
		}
	}
}
