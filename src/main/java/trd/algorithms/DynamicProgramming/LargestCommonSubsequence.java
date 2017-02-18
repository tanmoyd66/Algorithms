package trd.algorithms.DynamicProgramming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import trd.algorithms.utilities.CompositeKey;
import trd.algorithms.utilities.Tuples;

public class LargestCommonSubsequence {
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
	
	public static void main(String[] args) {
		
		if (true){
			String[] s1 = new String[] {"MAGICIAN", "ACCGGTCGAGTGCGCGGAAGCCGGCCGAA"};
			String[] s2 = new String[] {"MATHEMATICIAN", "GTCGTTCGGAATGCCGTTGCTCTGTAAA"};
			for (int i = 0; i < s1.length; i++) {
				System.out.printf("LCS(Memoized) of %s and %s is:[%s]\n", s1[i], s2[i], LargestCommonSubsequence_Memoized(s1[i], s2[i]));
				System.out.printf("LCS(Tabular)  of %s and %s is:[%s]\n", s1[i], s2[i], LargestCommonSubsequence_Tabular(s1[i], s2[i]));
			}
		}
	}
}
