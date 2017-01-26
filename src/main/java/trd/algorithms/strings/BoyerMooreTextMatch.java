package trd.algorithms.strings;

public class BoyerMooreTextMatch {
	
	// We will use an array to demonstrate. 
	// If you have a large alphabet, using a map is better.
	public static String alphabet = "abcdefghijklmnopqrstuvwxyz";
	public static int	 maxchars = 256;
	
	private Integer[] 	Right;
	private String 		Pattern;
	
	public BoyerMooreTextMatch(String Pattern) {
		this.Pattern = Pattern;
	}
	
	public void CompilePattern() {
		
		// The Right array contains the rightmost location of the character
		Right = new Integer[maxchars];
		for (int i = 0; i < Right.length; i++) 
			Right[i] = -1;
		for (int i = 0; i < Pattern.length(); i++) 
			Right[(int)Pattern.charAt(i)] = i;
	}
	
	public int MatchPattern(String Text) {
		int patLen = Pattern.length();
		int txtLen = Text.length();		
		int shift  = 0;
		
		while(shift <= (txtLen - patLen)) {
			int j = patLen - 1;
			
			// Match from the right until the characters match
			while (j >= 0 && Pattern.charAt(j) == Text.charAt(shift + j))
				j--;
        
			// If we are past the left end of the pattern, indicate the match
			if (j < 0)
				return shift;
			else {
				// We have not fallen off the left end and thence the text and pattern does not match
				// Align with the right-most occurrence of the bad character
				shift += Math.max(1, j - Right[(int)Text.charAt(shift + j)]);
			}
		}
		return -1;
	}
	
	public static void main(String[] args) {
		BoyerMooreTextMatch bm = new BoyerMooreTextMatch("ababaca");
		bm.CompilePattern();
		String[] texts = new String[] { "zzababzababacaz" };
		for (String text : texts) {
			int result = bm.MatchPattern(text);
			System.out.printf("Match in text: %s at: %d\n", text, result);
		}
	}
}
