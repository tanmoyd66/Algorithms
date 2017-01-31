package trd.algorithms.DynamicProgramming;

public class LevenshteinDistance {
	//-----------------------------------------------------------------------------------------------------
	// Levenshtein Distance between 2 strings: Recursive Formulation
	// 
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
	public static void main(String[] args) {
		if (true) {
	        String s1 = "kitten", s2 = "sitting";
	        System.out.printf("Levenshtein Distance between %s and %s is: (%d)\n", s1, s2, 
	        		LevenshteinDistance(s1.toCharArray(), s2.toCharArray(), s1.length(), s2.length()));
		}
	}
}
