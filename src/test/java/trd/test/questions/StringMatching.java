package trd.test.questions;

import trd.algorithms.utilities.ArrayPrint;

public class StringMatching {
	public static int BruteForce(String s1, String s2) {
		int i = 0, j = 0;
		for (i = 0, j = 0; i < s1.length() && j < s2.length(); i++) {
			if (s1.charAt(i) == s2.charAt(j)) {
				j++;
			} else {
				i -= j; j = 0;
			}
		}
		if (j == s2.length())
			return i - j;
		return -1;
	}

	public static Integer[] CompileKMP(String s2) {
		Integer[] A = new Integer[s2.length()];
		A[0] = 0;
		for (int j = 0, i = 1; i < s2.length();) {
			if (s2.charAt(i) == s2.charAt(j)) {
				A[i] = j + 1;
				i++; j++;
			} else {
				if (j == 0) {
					A[i] = 0; i++;
				} else {
					j = A[j - 1];
				}
			}
		}
		return A;
	}
	public static int KMP(String s1, String s2, Integer[] KMPTable) {
		int i = 0, j = 0;
		for (i = 0, j = 0; i < s1.length() && j < s2.length(); ) {
//			System.out.printf("Comparing: %s %s\n", s1.charAt(i), s2.charAt(j));
			if (s1.charAt(i) == s2.charAt(j)) {
				j++; i++;
			} else {
				if (j == 0) {
					i++;
				} else {
					j = KMPTable[j - 1];
				}
			}
		}
		if (j == s2.length())
			return i - j;
		return -1;
	}
	
	public static void main(String[] args) {
		String s1 = "abxabcabcaby", s2 = "abcaby";
		System.out.printf("Match at %d\n", BruteForce(s1, s2));
		
		Integer[] A = CompileKMP(s2);
		System.out.printf("%s\n", ArrayPrint.ArrayToString("", A));
		System.out.printf("Match at %d\n", KMP(s1, s2, A));
		
	}
}
