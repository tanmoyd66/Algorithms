package trd.algorithms.strings;

import java.util.HashMap;

import trd.algorithms.utilities.ArrayPrint;

// Given an array [cat,dog,dog,cat] the goal is to see if it follows the pattern abba
public class SimilarPatternsInTwoStrings {
	public static boolean FollowsPattern(String[] A, String pat) {
		if (A.length != pat.length())
			return false;
		
		HashMap<String, Character> wordToPattern = new HashMap<String, Character>();
		HashMap<Character, String> patternToWord = new HashMap<Character, String>();

		// Iterate over the strings
		for (int i = 0; i < pat.length(); i++) {
			
			Character 	registeredPatElement = wordToPattern.get(A[i]);
			String 		registeredString = patternToWord.get(pat.charAt(i));
			
			// Update both the hash tables
			if (registeredPatElement != null) {
				if (pat.charAt(i) != registeredPatElement)
					return false;
			} else 
				wordToPattern.put(A[i], pat.charAt(i));

			if (registeredString != null) {
				if (!A[i].equals(registeredString))
					return false;
			} else 
				patternToWord.put(pat.charAt(i), A[i]);
		}
		return true;
	}
	
	public static void main(String[] args) {
		String[] A = new String[] {"cat", "dog", "dog", "rocket" };
		String pat = "abbc";
		System.out.printf("Pattern %s matches %s: %s\n", pat, ArrayPrint.ArrayToString("", A), 
				FollowsPattern(A, pat));
	}
}
