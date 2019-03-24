package trd.algorithms.strings;

public class RegularExpressionMatching {
	// Match a regular expression containing * and .
	// *: Kleene Closure
	// .: Matches any character
	public static Boolean CheckIf(String text, String pattern) {
		if (pattern.isEmpty())
			return text.isEmpty();
		
		boolean match = !text.isEmpty() &&
				        text.charAt(0) == pattern.charAt(0) || pattern.charAt(0) == '.';
		if (pattern.length() >= 2 && pattern.charAt(1) == '*') {
			return CheckIf(text, pattern.substring(2)) ||
				   match && CheckIf(text.substring(1), pattern); 
		} else {
			return match && CheckIf(text.substring(1), pattern.substring(1));
		}
	}
	
	public static void main(String[] args) {
		String pattern = "a*cab";
		String[] input = {"aacab", "cab" };
		for (String s : input) {
			System.out.printf("Matching string [%s] with pattern [%s]: %s\n", s, pattern, CheckIf(s, pattern).toString());
		}
	}
}
