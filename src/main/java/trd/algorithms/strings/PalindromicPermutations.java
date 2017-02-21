package trd.algorithms.strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PalindromicPermutations {
	private static HashMap<String, Integer> unique_permutations = new HashMap<>();
	private static String unique_chars = "";

	
	private static boolean isPalindromePossible(String input) {
		int[] map = new int[128];
		boolean even = (0 == input.length() % 2);

		// update count of each character assuming ASCII
		for (int i = 0; i < input.length(); i++) {
			map[input.charAt(i)]++;
		}

		// go thru each character
		// for even length string the count of each character has to be a multiple of 2
		// for odd length string the count of each character can't be a multiple of 2
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (map[c] > 0) {
				if (even && map[c] % 2 != 0) {
					return false;
				} else if (!even && map[c] % 2 != 0) {
					even = true;
				}
			}
		}
		return true;
	}

	public static void Generate(String input) {
		
		// Check if palindromic permutations are at all possible
		if (isPalindromePossible(input)) {

			// Collect all unique characters into a string
			HashMap<Character, Integer> char_map = new HashMap<>();
			for (int i = 0; i < input.length(); i++) {
				char c = input.charAt(i);
				if (!char_map.containsKey(c)) {
					char_map.put(c, 1);
				} else {
					unique_chars += input.substring(i, i + 1);
					char_map.remove(c);
				}
			}

			// Generate permutations recursively
			generatePermutations(0, new ArrayList<StringBuilder>());

			
			for (Character c : char_map.keySet()) {
				unique_chars = c.toString();
			}

			if (char_map.size() > 0) {
				for (String key : unique_permutations.keySet()) {
					System.out.println(key + unique_chars + new StringBuilder(key).reverse().toString());
				}
			} else {
				for (String key : unique_permutations.keySet()) {
					System.out.println(key + new StringBuilder(key).reverse().toString());
				}
			}

		} else {
			System.out.println("No palindromic permutations possible");
		}

	}

	private static void generatePermutations(int index, ArrayList<StringBuilder> results) {
		if (index == 0) {
			// Base case of recursion
			// unique_chars stores the characters that have > 1 occurrences
			if (unique_chars.length() > 1) {
				
				// Recurse on the rest of the  
				results.add(new StringBuilder(unique_chars.substring(0, 1)));
				generatePermutations(index + 1, results);
			} else {
				unique_permutations.put(unique_chars.substring(0, 1), 1);
				return;
			}
		} else {
			ArrayList<StringBuilder> new_results = new ArrayList<>();
			for (StringBuilder result : results) {
				for (int i = 0; i <= result.length(); i++) {
					StringBuilder temp_result = new StringBuilder(result);
					new_results.add(temp_result.insert(i, unique_chars.charAt(index)));
				}
			}

			if (index == unique_chars.length() - 1) {
				for (StringBuilder result : new_results) {
					String str_result = result.toString();
					if (!unique_permutations.containsKey(str_result)) {
						unique_permutations.put(str_result, 1);
					}
				}
				return;
			}
			generatePermutations(index + 1, new_results);
		}
	}

	
	
	public static void main(String[] args) {
		System.out.println("Enter the string for checking");
		Scanner sc = new Scanner(System.in);
		String input = sc.next();
		Generate(input);
		sc.close();
	}
	
	
}
