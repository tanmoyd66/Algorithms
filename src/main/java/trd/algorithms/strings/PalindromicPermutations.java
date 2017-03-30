package trd.algorithms.strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import trd.algorithms.Arrays.Permutations;
import trd.algorithms.utilities.Swapper;
import trd.algorithms.utilities.Utilities;

public class PalindromicPermutations {
	
	// check if a string can have a palindromic permutation
	// Strategy: There can be at most 1 character that has an odd count 
	public static Character CheckIfPalindromizable(String s,
								Map<Character, Integer> charMap) {
		for (char c : s.toCharArray()) {
			Integer cCount = charMap.get(c);
			cCount = cCount == null ? 1 : (cCount + 1);
			charMap.put(c, cCount);
		}
		Character oddChar = null;
		for (Map.Entry<Character, Integer> me : charMap.entrySet()) {
			if (me.getValue() % 2 == 1) {
				if (oddChar != null && s.length() %2 == 0)
					return null;
				else 
					oddChar = me.getKey();
			}
		}
		return oddChar;
	}
	
	// Generate all permutations
	public static Set<String> GeneratePermutations(String s) {
		Map<Character, Integer> charMap = new HashMap<Character, Integer>();
		Set<String> ret = new HashSet<String>();
		
		// Step 1: Check if the String is palindromizable
		Character oddChar = CheckIfPalindromizable(s, charMap);		
		if (s.length() % 2 == 1 && oddChar == null ||
			s.length() % 2 == 0 && oddChar != null) 
			return ret;
		charMap.remove(oddChar);
		
		// Step 2: Find the characters that will make the string
		String charSet = "";
		for (Map.Entry<Character, Integer> me : charMap.entrySet()) {
			int count = me.getValue();
			if (count %2 == 0) {
				for (int j = 0; j < count / 2; j++)
					charSet += me.getKey().toString();
			}
		}
		
		// Step 3: Generate Permutations of the characters
		List<List<Character>> permList = new ArrayList<List<Character>>();
		Permutations.getPermutations(Utilities.StringToCharacterArray(charSet), 0, permList, 
								new Swapper.SwapperImpl<Character>());

		// Step 4: Generate the permuted strings
		for (List<Character> aPerm: permList) {
			StringBuilder sb = new StringBuilder();
			String thisPermPrefix = Utilities.CharacterListToString(aPerm);
			sb.append(thisPermPrefix);
			sb.append(oddChar == null ? "" : oddChar);
			Collections.reverse(aPerm);
			String thisPermSuffix = Utilities.CharacterListToString(aPerm);
			sb.append(thisPermSuffix);
			ret.add(sb.toString());
		}
		return ret;
	}
	
	public static void main(String[] args) {
		String s = "abab";
		System.out.printf("%s\n", GeneratePermutations(s));
	}
}
