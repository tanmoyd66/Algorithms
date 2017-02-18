package trd.algorithms.strings;

public class AssortedStringProblems {
	public static String getShortestPalindrome(String str) {
		StringBuilder builder = new StringBuilder(str).reverse();
		String revStr = builder.toString();
		int len = str.length();
		int k;
		
		for (k = len; k >= 0; k--) {
			String prefixOfOrig = str.substring(0, k);
			String suffixOfRev	= revStr.substring(len - k);
			if (prefixOfOrig.equals(suffixOfRev)) {
				break;
			}
		}
		String prefixOfRev = revStr.substring(0, len - k);
		return prefixOfRev + str;
	}
	
	public static void main(String[] args) {
		if (true) {
			String s = "abab";
			System.out.printf("%s: %s\n", s, getShortestPalindrome(s));
		}
	}
}
