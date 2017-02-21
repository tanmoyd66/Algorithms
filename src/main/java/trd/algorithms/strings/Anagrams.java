package trd.algorithms.strings;

import java.util.HashMap;

public class Anagrams {
	// Anagram is defined as a member of the set of permutations of s1
	// silent and listen are anagrams of one another
	// Strategy:
	// O(n Log n)
	//	  * You could sort - both strings should equal when sorted
	// O(n) strategy:
	//	  * The 2 strings have to be equal lengths
	//	  * For each character in s1 remove it from s2
	// 	  * In the end both strings should be empty
	public static boolean CheckIf(String s1, String s2) {
		HashMap<Character, Integer> charMap = new HashMap<Character, Integer>();
		
		// Put all characters from s1 into the charMap
		for (int i = 0; i < s1.length(); i++) {
			Integer count = charMap.get(s1.charAt(i));
			if (count == null)
				charMap.put(s1.charAt(i), 1);
			else 
				charMap.put(s1.charAt(i), count + 1);
		}
		
		// Scan s2, remove all characters from charMap
		for (int i = 0; i < s2.length(); i++) {
			Integer count = charMap.get(s2.charAt(i));
			if (count == null)
				return false;
			else {
				if (count == 1)
					charMap.remove(s2.charAt(i));
				else 
					charMap.put(s1.charAt(i), count - 1);
			}
		}
		
		// charMap should be empty if they are anagrams
		return charMap.size() == 0;
	}
	
	public static void main(String[] args) {
		String s1 = "silent", s2 = "listen";
		System.out.printf("%s and %s are anagrams: %s\n", s1, s2, Anagrams.CheckIf(s1, s2));
	}
}
