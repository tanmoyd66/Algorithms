package trd.algorithms.strings;

public class AssortedStringProblems {
	public static String getShortestPalindrome(String str) {
		StringBuilder builder = new StringBuilder(str).reverse();
		String revStr = builder.toString();
		int len = str.length();
		int k;
		for (k = len; k >= 0; k--) {
			if (str.substring(0, k).equals(revStr.substring(len - k))) {
				break;
			}
		}
		return revStr.substring(0, len - k) + str;
	}
	
	public static void main(String[] args) {
		if (true) {
			String s = "abab";
			System.out.printf("%s: %s\n", s, getShortestPalindrome(s));
		}
	}
}
