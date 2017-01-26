package trd.algorithms.strings;

public class BasicStringMatching {
	public static int BruteForceStringMatching1(String pat, String txt) {
		int patLen = pat.length();
		int txtLen = txt.length();
		
		// Move forward the text pointer and the pattern pointer.
		// If there is a mismatch, restart from the next text pointer
		for (int i = 0; i <= txtLen - patLen; i++) {
			int j;
			for (j = 0; j < patLen; j++)
				if (txt.charAt(i+j) != pat.charAt(j))
					break;
			if (j == patLen) 
				return i;
		}
		return -1;
	}

	public static int BruteForceStringMatching2(String pat, String txt) {
		int j, patLen = pat.length();
		int i, txtLen = txt.length();

		// Move forward the text pointer and the pattern pointer.
		// If there is a mismatch, restart from the next text pointer
		// We do it in one loop, just backing up the text pointer
		// It is instructional, as it is the same code we will use for KMP
		for (i = 0, j = 0; i < txtLen && j < patLen; i++) {
			if (txt.charAt(i) == pat.charAt(j)) 
				j++;
			else { 
				i -= j; j = 0; 
			}
		}
		if (j == patLen) 
			return i - patLen;
		else 
			return txtLen;
	}
	
	public static void main(String[] args) {
		String pattern = "ababac";
		String[] texts = new String[] { "zzababzababacz" };
		for (String text : texts) {
			int result1 = BruteForceStringMatching1(pattern, text);
			int result2 = BruteForceStringMatching2(pattern, text);
			System.out.printf("(Brute Force 1) Match in text: %s at: %d\n", text, result1);
			System.out.printf("(Brute Force 2) Match in text: %s at: %d\n", text, result2);
		}
	}
}
