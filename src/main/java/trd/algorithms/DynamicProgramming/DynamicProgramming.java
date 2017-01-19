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

import trd.algorithms.Arrays.ArrayProblems;
import trd.algorithms.Arrays.ArrayProblems.ISwapper;
import trd.algorithms.utilities.CompositeKey;
import trd.algorithms.utilities.Tuples;

public class DynamicProgramming {
	
	//-----------------------------------------------------------------------------------------------------
	// Largest Common Subsequence: Recursive Formulation with Memoization 
	private static List<Character> LargestCommonSubsequence_Memoized(char[] s1, char[] s2, int iS1, int iS2, 
					HashMap<CompositeKey<Integer,Integer>, List<Character>> MemoTable) {
		
		List<Character> ret = MemoTable.get(new CompositeKey<Integer,Integer>(iS1,iS2));
		//System.out.printf("LCS - %32s[%4d], %32s[%4d] (%s)\n", new String(s1), iS1, new String(s2), iS2, ret != null ? "hit" : "miss");
		if (ret != null)
			return ret;
		
		// Base of recursion
		if (iS1 < 0 || iS2 < 0) {
			ret = new ArrayList<Character>();
			MemoTable.put(new CompositeKey<Integer,Integer>(iS1,iS2), ret);
			return ret;
		}
		
		if (s1[iS1] == s2[iS2]) {
			
			// If equal, we push the character into the stack
			// We have to be careful in that we do not add to the list that we got from the recursive call
			// Make a clone of it
			ret = LargestCommonSubsequence_Memoized(s1, s2, iS1 - 1, iS2 - 1, MemoTable);
			if (ret == null)
				ret = new ArrayList<Character>();
			else {
				ret = Tuples.GetClone(ret);
			}

			MemoTable.put(new CompositeKey<Integer,Integer>(iS1,iS2), ret);
			ret.add(s1[iS1]);
			return ret;
		}
		else {
			// If unequal, we call recursively
			List<Character> result1 = LargestCommonSubsequence_Memoized(s1, s2, iS1 - 1, iS2, MemoTable);
			List<Character> result2 = LargestCommonSubsequence_Memoized(s1, s2, iS1, iS2 - 1, MemoTable);
			
			ret = (result1.size() > result2.size() ? result1 : result2);
			MemoTable.put(new CompositeKey<Integer,Integer>(iS1, iS2), ret);
			return ret;
		}
	}	
	public static String LargestCommonSubsequence_Memoized(String _s1, String _s2) {
		char[] s1 = _s1.toCharArray(), s2 = _s2.toCharArray();
		HashMap<CompositeKey<Integer,Integer>, List<Character>> MemoTable = new HashMap<>();
		List<Character> output = LargestCommonSubsequence_Memoized(s1, s2, s1.length - 1, s2.length - 1, MemoTable);		
		Character[] retVal = new Character[output.size()];
		output.toArray(retVal);
		return new String(Tuples.CharacterArrayToCharArray(retVal));
	}
	//-----------------------------------------------------------------------------------------------------
	// Largest Common Subsequence: Tabular Formulation 
	public enum LCS_Direction {Up, Left, Diag};
	public static String LargestCommonSubsequence_Tabular(String _s1, String _s2) {
		
		char[] s1 = _s1.toCharArray(), s2 = _s2.toCharArray();
		
		// We keep two arrays: One for Cost Matrix and another for Tracking
		Integer[][] 		C = new Integer[s1.length + 1][s2.length + 1];
		LCS_Direction[][] 	D = new LCS_Direction[s1.length + 1][s2.length + 1];
		
		// Initialization: Set the cost matrix to 0 for the first row and first column
		for (int j = 0; j < s2.length + 1; j++)
			C[0][j] = 0;
		for (int i = 0; i < s1.length + 1; i++)
			C[i][0] = 0;
		
		// Now complete the cost matrix registering in D, the direction when we travel back
		for (int i = 1; i < s1.length + 1; i++) {
			for (int j = 1; j < s2.length + 1; j++) {
				if (s1[i - 1] == s2[j - 1]) {
					C[i][j] = C[i - 1][j - 1] + 1;
					D[i][j] = LCS_Direction.Diag;
				} else if (C[i - 1][j] > C[i][j-1]) {
					C[i][j] = C[i - 1][j];
					D[i][j] = LCS_Direction.Up;
				} else {
					C[i][j] = C[i][j - 1];
					D[i][j] = LCS_Direction.Left;
				}
			}
		}
		
		// Track back the arrows to get the list
		LinkedList<Character> lcs = new LinkedList<Character>();
		int row = s1.length, col = s2.length;
		while (row > 0 && col > 0) {
			switch (D[row][col]) {
			case Left: 	col--; continue;
			case Up: 	row--; continue;
			case Diag: 	lcs.add(0, s1[row - 1]); col--; row--; continue;
			}
		}
		return new String(Tuples.CharacterListToCharArray(lcs));
	}
	
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
	// Knapsack: Memoized Formulation
	public static class KnapsackConfig {
		Integer[] 	Values;
		Integer[] 	Weights;
		HashMap<CompositeKey<Integer,Integer>, KnapsackResults> MemoTable = new HashMap<>();
		public KnapsackConfig(Integer[] Values, Integer[] Weights) {
			this.Values = Values; this.Weights = Weights;
		}
	}
	public static class KnapsackResults {
		List<Integer> 	Elements;
		Integer			CurrentValue;
		Integer			CurrentCapacity;
		public KnapsackResults() {
			Elements = new ArrayList<Integer>();
			CurrentValue = CurrentCapacity = 0;
		}
	}
	private static KnapsackResults KnapsackMemoized(KnapsackConfig kc, Integer capacity, int idx) {
		
		// Short circuit by looking up the memo-table
		KnapsackResults ret = kc.MemoTable.get(new CompositeKey<>(idx,capacity));
		if (ret != null)
			return ret;
		
		ret = new KnapsackResults();
		kc.MemoTable.put(new CompositeKey<>(idx,capacity), ret);
		
		// Base of recursion
		if (idx == 0) {
			if (capacity >= kc.Weights[idx]) {
				ret.Elements.add(idx); ret.CurrentCapacity = kc.Weights[idx]; ret.CurrentValue = kc.Values[idx];
				return ret;
			} else 
				return ret;
		} else if (idx < 0) {
			return ret;
		} else {
			// Inductive Step
			KnapsackResults ret1 = KnapsackMemoized(kc, capacity, idx - 1);
			KnapsackResults ret2 = KnapsackMemoized(kc, capacity - kc.Weights[idx], idx - 1);
			if ((ret1.CurrentValue > (ret2.CurrentValue + kc.Values[idx])) || ((ret2.CurrentCapacity + kc.Weights[idx]) > capacity))
				return ret1;
			ret2.CurrentValue    += kc.Values[idx]; 
			ret2.CurrentCapacity += kc.Weights[idx]; 
			ret2.Elements.add(idx);
			return ret2;
		}
	}
	public static void KnapsackMemoized(Integer[] values, Integer[] weights, int capacity) {
		KnapsackConfig  kc = new KnapsackConfig(values, weights);
		KnapsackResults kr = KnapsackMemoized(kc, capacity, values.length - 1);
        System.out.printf("Knapsack of %s,%s with capacity %d is: (%d)%s\n", 
        		ArrayProblems.ArrayToString("Values", values), ArrayProblems.ArrayToString("Weights", weights), capacity, kr.CurrentValue, kr.Elements); 
	}
	//-----------------------------------------------------------------------------------------------------
	// Knapsack: Bottom-up Formulation
	private static List<Integer> TraceBack(Integer[][] C, Integer[] values) {
		int rows = C.length, cols = C[0].length;
		int maxVal = C[rows - 1][cols - 1];
		int col = cols - 1, row = 1;
		List<Integer> ret = new ArrayList<Integer>(); 
		for ( ; col >=0 && row < rows; ) {
			if (C[row][col] == maxVal) {
				ret.add(row - 1);
				maxVal = maxVal - values[row - 1];
				if (maxVal <= 0) 
					break;
				while (C[rows - 1][col] > maxVal) col--;
				row = 1;
			} else {
				row++;
			}
		}
		return ret;
	}	
	public static void KnapsackBottomUp(Integer[] values, Integer[] weights, int capacity) {

		// O(nW) space where n = number of items and W = capacity
		Integer[][] C = new Integer[values.length + 1][capacity + 1];
		
		// Initialization.
		for (int j = 0; j < C[0].length; j++)
			C[0][j] = 0;
		
		// Fill the array
		for (int i = 1; i < values.length + 1; i++) {
			for (int j = 0; j < capacity + 1; j++) {
				// We will compute the max(A[i-1,j], A[i-1][j-W[i-1]]
				// j-W[i-1] might be less than 0. To avoid this, we will set that to 0 if less than 0 
				int colWithReducedCapacity = j - weights[i - 1] < 0 ? 0 : j - weights[i - 1];
				int res1 = C[i - 1][j];
				int res2 = weights[i - 1] <= j ?	// Can only do this, if the item fits 
								(C[i - 1][colWithReducedCapacity] + values[i - 1]) : 0;
				C[i][j] = Math.max(res1, res2);
			}
		}
		
		//System.out.printf("%s\n", ArrayProblems.MatrixToString(C, values.length + 1, capacity + 1));		
		// Trace back from the right.
		List<Integer> traceBack = TraceBack(C, values);
		
		System.out.printf("Knapsack of %s,%s with capacity %d is: (%d)%s\n", 
        		ArrayProblems.ArrayToString("Values", values), ArrayProblems.ArrayToString("Weights", weights), capacity, 
        		traceBack.stream().mapToInt(i -> values[i]).sum(), 
        		traceBack); 
	}
	//-----------------------------------------------------------------------------------------------------
	// Maximal Value By Parenthesization (Recursive Formulation)
	public static class OptimalMaxParenthesization_Config {
		String[] 	Operators;
		Integer[] 	Operands;		
		public OptimalMaxParenthesization_Config(String[] Operators, Integer[] Operands) {
			this.Operators = Operators; this.Operands = Operands;
		}
	}
	public static class OptimalMaxParenthesization_Result {
		Integer		Result;
		String		stringizedResult = "";
		public OptimalMaxParenthesization_Result(Integer Result, String stringizedResult) {
			this.Result = Result; this.stringizedResult = stringizedResult;
		}
	}
	public static Integer getValue(String operator, Integer lhs, Integer rhs) {
		switch (operator) {
		case "+": return lhs + rhs;
		case "-": return lhs - rhs;
		case "*": return lhs * rhs;
		case "/": return lhs / rhs;
		}
		return 0;
	}
	public static OptimalMaxParenthesization_Result OptimalMaxParenthesization(OptimalMaxParenthesization_Config ompc, int pos){
		if (pos == 0) {
			Integer Result = getValue(ompc.Operators[0], ompc.Operands[0], ompc.Operands[1]);
			String  stringizedResult = "(" + ompc.Operands[0] + ompc.Operators[0] + ompc.Operands[1] + ")";
			return new OptimalMaxParenthesization_Result(Result, stringizedResult);
		} else if (pos < 0) {
			return null;
		} else {
			OptimalMaxParenthesization_Result res1 = OptimalMaxParenthesization(ompc, pos - 1);
			OptimalMaxParenthesization_Result res2 = OptimalMaxParenthesization(ompc, pos - 2);

			if (res1 == null)
				return null;
			
			res1.Result = getValue(ompc.Operators[pos], res1.Result, ompc.Operands[pos + 1]);
			res1.stringizedResult = "(" + res1.stringizedResult + ompc.Operators[pos] + ompc.Operands[pos + 1] + ")";
			if (res2 == null)
				return res1;
			
			res2.Result = getValue(ompc.Operators[pos - 1], res2.Result, getValue(ompc.Operators[pos], ompc.Operands[pos], ompc.Operands[pos + 1]));
			res2.stringizedResult = "(" + res2.stringizedResult + ompc.Operators[pos - 1] + "(" + ompc.Operands[pos] + ompc.Operators[pos] + ompc.Operands[pos + 1] + ")" + ")";
			return res2.Result > res1.Result ? res2 : res1;
		}
	}
	private static String getExpressionFromOperatorOperandArrays(String[] Operators, Integer[] Operands) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < Operators.length; i++) {
			sb.append(String.format("%d %s ", Operands[i], Operators[i]));
		}
		sb.append(String.format("%d]", Operands[Operands.length - 1]));
		return sb.toString();
	}
	public static void OptimalMaxParenthesization(String[] Operators, Integer[] Operands) {
		OptimalMaxParenthesization_Result ret = OptimalMaxParenthesization(new OptimalMaxParenthesization_Config(Operators, Operands), Operators.length - 1);
        System.out.printf("Maximal Value of %s is: %s = %d\n", getExpressionFromOperatorOperandArrays(Operators, Operands), ret.stringizedResult, ret.Result);
 	}
	//-----------------------------------------------------------------------------------------------------
	
	//-----------------------------------------------------------------------------------------------------
	// Levenshtein Distance between 2 strings: Recursive Formulation
	public static int LevenshteinDistance(char[] s1, char[] s2, int s1Idx, int s2Idx) {
		if (s1Idx == 0 && s2Idx == 0) {
			return 0;
		} else if (s1Idx == 0) {
			return s2Idx; 
		} else if (s2Idx == 0) {
			return s1Idx;
		} else {
			int res1 = (s1[s1Idx - 1] != s2[s2Idx - 1] ? 1 : 0) + LevenshteinDistance(s1, s2, s1Idx - 1, s2Idx - 1);
			int res2 = LevenshteinDistance(s1, s2, s1Idx - 1, s2Idx) + 1;
			int res3 = LevenshteinDistance(s1, s2, s1Idx, s2Idx - 1) + 1;
			return Math.min(res1, Math.min(res2, res3));
		}
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
    // Given a dictionary of words and a string of characters find a sentence
    // Dictionary: { "i", "like", "sam", "sung", "samsung", "mobile", "ice", "cream", "icecream", "man", "go", "mango", "and"}
    private static LinkedList<String> BreakIntoWords(Set<String> dictionary, String sentence) {
    	//System.out.println(sentence);
    	LinkedList<String> ret = new LinkedList<String>();
    	int size = sentence.length();
    	boolean fContinue = true;
    	for (int i = 0; fContinue && i < size; i++) {
    		String prefix = sentence.substring(0, i + 1);
    		String suffix = sentence.substring(i + 1, size);
    		if (dictionary.contains(prefix)) {
    			if (suffix.length() > 0) {
	    			List<String> retRec = BreakIntoWords(dictionary, suffix);
	    			if (!retRec.isEmpty()) {
	    				for (String word : retRec)
	    					ret.add(word);
	    				ret.add(prefix);
	    				fContinue = false;
	    			}
    			} else {
    				ret.add(prefix);
    			}
    		}
    	}
    	return ret;
    }
	//-----------------------------------------------------------------------------------------------------
    
	public static void main(String[] args) {
		
		if (true){
			String s1 = "MAGICIAN", s2 = "MATHEMATICIAN";
			System.out.printf("Largest Common Subsequence(Memoized) of %s and %s is:[%s]\n", s1, s2, LargestCommonSubsequence_Memoized(s1, s2));
			System.out.printf("Largest Common Subsequence(Tabular)  of %s and %s is:[%s]\n", s1, s2, LargestCommonSubsequence_Tabular(s1, s2));
			s1 = "ACCGGTCGAGTGCGCGGAAGCCGGCCGAA"; s2 = "GTCGTTCGGAATGCCGTTGCTCTGTAAA";
			System.out.printf("Largest Common Subsequence(Memoized) of %s and %s is:[%s]\n", s1, s2, LargestCommonSubsequence_Memoized(s1, s2));
			System.out.printf("Largest Common Subsequence(Tabular)  of %s and %s is:[%s]\n", s1, s2, LargestCommonSubsequence_Tabular(s1, s2));
		}
		
		
		if (true){
			Integer[] A = {1, 4, 5, 4};
			List<Integer> wis = MaximalWeightedIndependentSet(A, A.length - 1);
			System.out.printf("MaximalWeightedIndependentSubset of %s is:%s=%d\n", ArrayProblems.ArrayToString("", A), wis, wis.stream().reduce(0, (x,y)->(x+y)));
		}
		
		if (true) {
	        String inputString = "ABCDE";
	        List<String> output = new ArrayList<String>();
	        Character[] charArray = Lists.newLinkedList(Chars.asList(inputString.toCharArray())).toArray(new Character[0]);
	        Permute(charArray, 0, inputString.length()-1, output, new ArrayProblems.Swapper<Character>());
	        System.out.printf("Permutations of %s is: (%d) %s\n", inputString, output.size(), output); 
		}
		
		if (true) {
			KnapsackMemoized(new Integer[] {3, 2, 4, 4}, new Integer[] {4, 3, 2, 3}, 6);
			KnapsackBottomUp(new Integer[] {3, 2, 4, 4}, new Integer[] {4, 3, 2, 3}, 6);
			OptimalMaxParenthesization(new String[] {"+", "*", "-"}, new Integer[] {1, 5, 6, 1});
		}
		
		if (true) {
	        String s1 = "kitten", s2 = "sitting";
	        System.out.printf("Levenshtein Distance between %s and %s is: (%d)\n", s1, s2, 
	        		LevenshteinDistance(s1.toCharArray(), s2.toCharArray(), s1.length(), s2.length()));
		}

		if (true) {
			String[] dict = new String[] { "i", "like", "sam", "sung", "samsung", "mobile", "ice", "cream", "icecream", "man", "go", "mango", "and"};
	        String[] s1   = new String[] {"ilikesamsung", "iiiiiiii", "ilikelikeimangoiii", "samsungandmango", "samsungandmangok"};
	        for (String s : s1) {
	        	List<String>  ret = BreakIntoWords(Stream.of(dict).collect(Collectors.toSet()), s);
	        	Collections.reverse(ret);
	        	System.out.printf("Words in %s are: %s\n", s, ret);
	        }
		}
	}
}
